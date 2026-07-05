jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentInvoice, PaymentInvoice } from '../payment-invoice.model';
import { PaymentInvoiceService } from '../service/payment-invoice.service';

import { PaymentInvoiceRoutingResolveService } from './payment-invoice-routing-resolve.service';

describe('PaymentInvoice routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PaymentInvoiceRoutingResolveService;
  let service: PaymentInvoiceService;
  let resultPaymentInvoice: IPaymentInvoice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PaymentInvoiceRoutingResolveService);
    service = TestBed.inject(PaymentInvoiceService);
    resultPaymentInvoice = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentInvoice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInvoice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentInvoice).toEqual({ id: 123 });
    });

    it('should return new IPaymentInvoice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInvoice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentInvoice).toEqual(new PaymentInvoice());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentInvoice })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentInvoice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentInvoice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
