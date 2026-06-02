jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentCategory, PaymentCategory } from '../payment-category.model';
import { PaymentCategoryService } from '../service/payment-category.service';

import { PaymentCategoryRoutingResolveService } from './payment-category-routing-resolve.service';

describe('PaymentCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PaymentCategoryRoutingResolveService;
  let service: PaymentCategoryService;
  let resultPaymentCategory: IPaymentCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PaymentCategoryRoutingResolveService);
    service = TestBed.inject(PaymentCategoryService);
    resultPaymentCategory = undefined;
  });

  describe('resolve', () => {
    it('should return IPaymentCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentCategory).toEqual({ id: 123 });
    });

    it('should return new IPaymentCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaymentCategory).toEqual(new PaymentCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PaymentCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPaymentCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaymentCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
