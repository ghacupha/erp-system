jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDerivativeSubType, DerivativeSubType } from '../derivative-sub-type.model';
import { DerivativeSubTypeService } from '../service/derivative-sub-type.service';

import { DerivativeSubTypeRoutingResolveService } from './derivative-sub-type-routing-resolve.service';

describe('DerivativeSubType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DerivativeSubTypeRoutingResolveService;
  let service: DerivativeSubTypeService;
  let resultDerivativeSubType: IDerivativeSubType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DerivativeSubTypeRoutingResolveService);
    service = TestBed.inject(DerivativeSubTypeService);
    resultDerivativeSubType = undefined;
  });

  describe('resolve', () => {
    it('should return IDerivativeSubType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeSubType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDerivativeSubType).toEqual({ id: 123 });
    });

    it('should return new IDerivativeSubType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeSubType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDerivativeSubType).toEqual(new DerivativeSubType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DerivativeSubType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeSubType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDerivativeSubType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
