jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFxTransactionRateType, FxTransactionRateType } from '../fx-transaction-rate-type.model';
import { FxTransactionRateTypeService } from '../service/fx-transaction-rate-type.service';

import { FxTransactionRateTypeRoutingResolveService } from './fx-transaction-rate-type-routing-resolve.service';

describe('FxTransactionRateType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FxTransactionRateTypeRoutingResolveService;
  let service: FxTransactionRateTypeService;
  let resultFxTransactionRateType: IFxTransactionRateType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FxTransactionRateTypeRoutingResolveService);
    service = TestBed.inject(FxTransactionRateTypeService);
    resultFxTransactionRateType = undefined;
  });

  describe('resolve', () => {
    it('should return IFxTransactionRateType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxTransactionRateType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxTransactionRateType).toEqual({ id: 123 });
    });

    it('should return new IFxTransactionRateType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxTransactionRateType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFxTransactionRateType).toEqual(new FxTransactionRateType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FxTransactionRateType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxTransactionRateType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxTransactionRateType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
