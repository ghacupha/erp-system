jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetAccessory, AssetAccessory } from '../asset-accessory.model';
import { AssetAccessoryService } from '../service/asset-accessory.service';

import { AssetAccessoryRoutingResolveService } from './asset-accessory-routing-resolve.service';

describe('AssetAccessory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetAccessoryRoutingResolveService;
  let service: AssetAccessoryService;
  let resultAssetAccessory: IAssetAccessory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetAccessoryRoutingResolveService);
    service = TestBed.inject(AssetAccessoryService);
    resultAssetAccessory = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetAccessory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAccessory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAccessory).toEqual({ id: 123 });
    });

    it('should return new IAssetAccessory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAccessory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetAccessory).toEqual(new AssetAccessory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetAccessory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetAccessory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetAccessory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
