jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICustomerType, CustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';

import { CustomerTypeRoutingResolveService } from './customer-type-routing-resolve.service';

describe('CustomerType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CustomerTypeRoutingResolveService;
  let service: CustomerTypeService;
  let resultCustomerType: ICustomerType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CustomerTypeRoutingResolveService);
    service = TestBed.inject(CustomerTypeService);
    resultCustomerType = undefined;
  });

  describe('resolve', () => {
    it('should return ICustomerType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerType).toEqual({ id: 123 });
    });

    it('should return new ICustomerType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCustomerType).toEqual(new CustomerType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CustomerType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
