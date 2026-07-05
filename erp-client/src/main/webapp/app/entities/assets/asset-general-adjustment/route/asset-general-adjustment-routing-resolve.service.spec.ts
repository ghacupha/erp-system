jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetGeneralAdjustment, AssetGeneralAdjustment } from '../asset-general-adjustment.model';
import { AssetGeneralAdjustmentService } from '../service/asset-general-adjustment.service';

import { AssetGeneralAdjustmentRoutingResolveService } from './asset-general-adjustment-routing-resolve.service';

describe('AssetGeneralAdjustment routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetGeneralAdjustmentRoutingResolveService;
  let service: AssetGeneralAdjustmentService;
  let resultAssetGeneralAdjustment: IAssetGeneralAdjustment | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetGeneralAdjustmentRoutingResolveService);
    service = TestBed.inject(AssetGeneralAdjustmentService);
    resultAssetGeneralAdjustment = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetGeneralAdjustment returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetGeneralAdjustment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetGeneralAdjustment).toEqual({ id: 123 });
    });

    it('should return new IAssetGeneralAdjustment if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetGeneralAdjustment = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetGeneralAdjustment).toEqual(new AssetGeneralAdjustment());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetGeneralAdjustment })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetGeneralAdjustment = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetGeneralAdjustment).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
