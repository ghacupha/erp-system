jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFraudType, FraudType } from '../fraud-type.model';
import { FraudTypeService } from '../service/fraud-type.service';

import { FraudTypeRoutingResolveService } from './fraud-type-routing-resolve.service';

describe('FraudType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FraudTypeRoutingResolveService;
  let service: FraudTypeService;
  let resultFraudType: IFraudType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FraudTypeRoutingResolveService);
    service = TestBed.inject(FraudTypeService);
    resultFraudType = undefined;
  });

  describe('resolve', () => {
    it('should return IFraudType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFraudType).toEqual({ id: 123 });
    });

    it('should return new IFraudType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFraudType).toEqual(new FraudType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FraudType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFraudType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFraudType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
