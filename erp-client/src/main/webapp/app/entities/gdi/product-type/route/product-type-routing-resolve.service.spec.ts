jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProductType, ProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

import { ProductTypeRoutingResolveService } from './product-type-routing-resolve.service';

describe('ProductType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ProductTypeRoutingResolveService;
  let service: ProductTypeService;
  let resultProductType: IProductType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ProductTypeRoutingResolveService);
    service = TestBed.inject(ProductTypeService);
    resultProductType = undefined;
  });

  describe('resolve', () => {
    it('should return IProductType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductType).toEqual({ id: 123 });
    });

    it('should return new IProductType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProductType).toEqual(new ProductType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultProductType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProductType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
