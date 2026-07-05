jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetWarranty, AssetWarranty } from '../asset-warranty.model';
import { AssetWarrantyService } from '../service/asset-warranty.service';

import { AssetWarrantyRoutingResolveService } from './asset-warranty-routing-resolve.service';

describe('AssetWarranty routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetWarrantyRoutingResolveService;
  let service: AssetWarrantyService;
  let resultAssetWarranty: IAssetWarranty | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetWarrantyRoutingResolveService);
    service = TestBed.inject(AssetWarrantyService);
    resultAssetWarranty = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetWarranty returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWarranty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetWarranty).toEqual({ id: 123 });
    });

    it('should return new IAssetWarranty if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWarranty = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetWarranty).toEqual(new AssetWarranty());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetWarranty })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWarranty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetWarranty).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
