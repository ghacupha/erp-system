jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';
import { PaymentCalculationService } from '../service/payment-calculation.service';

import { PaymentCalculationRoutingResolveService } from './payment-calculation-routing-resolve.service';

describe('Service Tests', () => {
  describe('PaymentCalculation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PaymentCalculationRoutingResolveService;
    let service: PaymentCalculationService;
    let resultPaymentCalculation: IPaymentCalculation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PaymentCalculationRoutingResolveService);
      service = TestBed.inject(PaymentCalculationService);
      resultPaymentCalculation = undefined;
    });

    describe('resolve', () => {
      it('should return IPaymentCalculation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentCalculation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentCalculation).toEqual({ id: 123 });
      });

      it('should return new IPaymentCalculation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentCalculation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPaymentCalculation).toEqual(new PaymentCalculation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentCalculation })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentCalculation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentCalculation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
