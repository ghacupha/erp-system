jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentLabel, PaymentLabel } from '../payment-label.model';
import { PaymentLabelService } from '../service/payment-label.service';

import { PaymentLabelRoutingResolveService } from './payment-label-routing-resolve.service';

describe('Service Tests', () => {
  describe('PaymentLabel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PaymentLabelRoutingResolveService;
    let service: PaymentLabelService;
    let resultPaymentLabel: IPaymentLabel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PaymentLabelRoutingResolveService);
      service = TestBed.inject(PaymentLabelService);
      resultPaymentLabel = undefined;
    });

    describe('resolve', () => {
      it('should return IPaymentLabel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentLabel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentLabel).toEqual({ id: 123 });
      });

      it('should return new IPaymentLabel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentLabel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPaymentLabel).toEqual(new PaymentLabel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentLabel })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentLabel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentLabel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
