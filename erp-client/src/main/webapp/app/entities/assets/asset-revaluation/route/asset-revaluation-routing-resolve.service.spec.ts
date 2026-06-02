jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAssetRevaluation, AssetRevaluation } from '../asset-revaluation.model';
import { AssetRevaluationService } from '../service/asset-revaluation.service';

import { AssetRevaluationRoutingResolveService } from './asset-revaluation-routing-resolve.service';

describe('AssetRevaluation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AssetRevaluationRoutingResolveService;
  let service: AssetRevaluationService;
  let resultAssetRevaluation: IAssetRevaluation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AssetRevaluationRoutingResolveService);
    service = TestBed.inject(AssetRevaluationService);
    resultAssetRevaluation = undefined;
  });

  describe('resolve', () => {
    it('should return IAssetRevaluation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetRevaluation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetRevaluation).toEqual({ id: 123 });
    });

    it('should return new IAssetRevaluation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetRevaluation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAssetRevaluation).toEqual(new AssetRevaluation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AssetRevaluation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAssetRevaluation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAssetRevaluation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
