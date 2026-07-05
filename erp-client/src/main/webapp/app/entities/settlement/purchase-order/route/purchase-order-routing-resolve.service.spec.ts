jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { PurchaseOrderService } from '../service/purchase-order.service';

import { PurchaseOrderRoutingResolveService } from './purchase-order-routing-resolve.service';

describe('PurchaseOrder routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PurchaseOrderRoutingResolveService;
  let service: PurchaseOrderService;
  let resultPurchaseOrder: IPurchaseOrder | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PurchaseOrderRoutingResolveService);
    service = TestBed.inject(PurchaseOrderService);
    resultPurchaseOrder = undefined;
  });

  describe('resolve', () => {
    it('should return IPurchaseOrder returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPurchaseOrder).toEqual({ id: 123 });
    });

    it('should return new IPurchaseOrder if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseOrder = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPurchaseOrder).toEqual(new PurchaseOrder());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PurchaseOrder })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPurchaseOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPurchaseOrder).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
