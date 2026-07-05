jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';

import { ExchangeRateRoutingResolveService } from './exchange-rate-routing-resolve.service';

describe('ExchangeRate routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ExchangeRateRoutingResolveService;
  let service: ExchangeRateService;
  let resultExchangeRate: IExchangeRate | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ExchangeRateRoutingResolveService);
    service = TestBed.inject(ExchangeRateService);
    resultExchangeRate = undefined;
  });

  describe('resolve', () => {
    it('should return IExchangeRate returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExchangeRate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExchangeRate).toEqual({ id: 123 });
    });

    it('should return new IExchangeRate if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExchangeRate = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExchangeRate).toEqual(new ExchangeRate());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExchangeRate })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExchangeRate = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExchangeRate).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
