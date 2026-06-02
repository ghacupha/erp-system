jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetWriteOff, AssetWriteOff } from '../asset-write-off.model';
import { AssetWriteOffService } from '../service/asset-write-off.service';

import { AssetWriteOffRoutingResolveService } from './asset-write-off-routing-resolve.service';

describe('AssetWriteOff routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetWriteOffRoutingResolveService;
  let service: AssetWriteOffService;
  let resultAssetWriteOff: IAssetWriteOff | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetWriteOffRoutingResolveService);
    service = TestBed.inject(AssetWriteOffService);
    resultAssetWriteOff = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetWriteOff returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWriteOff = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetWriteOff).toEqual({ id: 123 });
    });

    it('should return new IAssetWriteOff if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWriteOff = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetWriteOff).toEqual(new AssetWriteOff());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetWriteOff })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetWriteOff = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetWriteOff).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
