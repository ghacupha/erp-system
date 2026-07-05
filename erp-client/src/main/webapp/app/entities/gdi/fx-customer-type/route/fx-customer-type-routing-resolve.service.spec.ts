jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';
import { FxCustomerTypeService } from '../service/fx-customer-type.service';

import { FxCustomerTypeRoutingResolveService } from './fx-customer-type-routing-resolve.service';

describe('FxCustomerType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FxCustomerTypeRoutingResolveService;
  let service: FxCustomerTypeService;
  let resultFxCustomerType: IFxCustomerType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FxCustomerTypeRoutingResolveService);
    service = TestBed.inject(FxCustomerTypeService);
    resultFxCustomerType = undefined;
  });

  describe('resolve', () => {
    it('should return IFxCustomerType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxCustomerType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxCustomerType).toEqual({ id: 123 });
    });

    it('should return new IFxCustomerType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxCustomerType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFxCustomerType).toEqual(new FxCustomerType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FxCustomerType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxCustomerType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxCustomerType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
