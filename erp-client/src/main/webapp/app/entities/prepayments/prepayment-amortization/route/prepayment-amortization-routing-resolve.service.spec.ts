jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrepaymentAmortization, PrepaymentAmortization } from '../prepayment-amortization.model';
import { PrepaymentAmortizationService } from '../service/prepayment-amortization.service';

import { PrepaymentAmortizationRoutingResolveService } from './prepayment-amortization-routing-resolve.service';

describe('PrepaymentAmortization routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PrepaymentAmortizationRoutingResolveService;
  let service: PrepaymentAmortizationService;
  let resultPrepaymentAmortization: IPrepaymentAmortization | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PrepaymentAmortizationRoutingResolveService);
    service = TestBed.inject(PrepaymentAmortizationService);
    resultPrepaymentAmortization = undefined;
  });

  describe('resolve', () => {
    it('should return IPrepaymentAmortization returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentAmortization = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentAmortization).toEqual({ id: 123 });
    });

    it('should return new IPrepaymentAmortization if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentAmortization = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPrepaymentAmortization).toEqual(new PrepaymentAmortization());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PrepaymentAmortization })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentAmortization = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentAmortization).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
