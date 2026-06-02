jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICustomerIDDocumentType, CustomerIDDocumentType } from '../customer-id-document-type.model';
import { CustomerIDDocumentTypeService } from '../service/customer-id-document-type.service';

import { CustomerIDDocumentTypeRoutingResolveService } from './customer-id-document-type-routing-resolve.service';

describe('CustomerIDDocumentType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CustomerIDDocumentTypeRoutingResolveService;
  let service: CustomerIDDocumentTypeService;
  let resultCustomerIDDocumentType: ICustomerIDDocumentType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CustomerIDDocumentTypeRoutingResolveService);
    service = TestBed.inject(CustomerIDDocumentTypeService);
    resultCustomerIDDocumentType = undefined;
  });

  describe('resolve', () => {
    it('should return ICustomerIDDocumentType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerIDDocumentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerIDDocumentType).toEqual({ id: 123 });
    });

    it('should return new ICustomerIDDocumentType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerIDDocumentType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCustomerIDDocumentType).toEqual(new CustomerIDDocumentType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CustomerIDDocumentType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerIDDocumentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerIDDocumentType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
