jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMerchantType, MerchantType } from '../merchant-type.model';
import { MerchantTypeService } from '../service/merchant-type.service';

import { MerchantTypeRoutingResolveService } from './merchant-type-routing-resolve.service';

describe('MerchantType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MerchantTypeRoutingResolveService;
  let service: MerchantTypeService;
  let resultMerchantType: IMerchantType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MerchantTypeRoutingResolveService);
    service = TestBed.inject(MerchantTypeService);
    resultMerchantType = undefined;
  });

  describe('resolve', () => {
    it('should return IMerchantType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMerchantType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMerchantType).toEqual({ id: 123 });
    });

    it('should return new IMerchantType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMerchantType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMerchantType).toEqual(new MerchantType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MerchantType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMerchantType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMerchantType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
