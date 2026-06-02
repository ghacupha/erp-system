jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetCategory, AssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';

import { AssetCategoryRoutingResolveService } from './asset-category-routing-resolve.service';

describe('AssetCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetCategoryRoutingResolveService;
  let service: AssetCategoryService;
  let resultAssetCategory: IAssetCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetCategoryRoutingResolveService);
    service = TestBed.inject(AssetCategoryService);
    resultAssetCategory = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetCategory).toEqual({ id: 123 });
    });

    it('should return new IAssetCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetCategory).toEqual(new AssetCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
