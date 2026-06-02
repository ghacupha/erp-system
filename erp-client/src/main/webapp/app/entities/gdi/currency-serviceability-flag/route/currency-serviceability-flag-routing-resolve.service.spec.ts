jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICurrencyServiceabilityFlag, CurrencyServiceabilityFlag } from '../currency-serviceability-flag.model';
import { CurrencyServiceabilityFlagService } from '../service/currency-serviceability-flag.service';

import { CurrencyServiceabilityFlagRoutingResolveService } from './currency-serviceability-flag-routing-resolve.service';

describe('CurrencyServiceabilityFlag routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CurrencyServiceabilityFlagRoutingResolveService;
  let service: CurrencyServiceabilityFlagService;
  let resultCurrencyServiceabilityFlag: ICurrencyServiceabilityFlag | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CurrencyServiceabilityFlagRoutingResolveService);
    service = TestBed.inject(CurrencyServiceabilityFlagService);
    resultCurrencyServiceabilityFlag = undefined;
  });

  describe('resolve', () => {
    it('should return ICurrencyServiceabilityFlag returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCurrencyServiceabilityFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCurrencyServiceabilityFlag).toEqual({ id: 123 });
    });

    it('should return new ICurrencyServiceabilityFlag if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCurrencyServiceabilityFlag = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCurrencyServiceabilityFlag).toEqual(new CurrencyServiceabilityFlag());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CurrencyServiceabilityFlag })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCurrencyServiceabilityFlag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCurrencyServiceabilityFlag).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
