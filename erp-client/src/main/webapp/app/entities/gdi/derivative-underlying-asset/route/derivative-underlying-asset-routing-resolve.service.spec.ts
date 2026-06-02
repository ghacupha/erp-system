jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDerivativeUnderlyingAsset, DerivativeUnderlyingAsset } from '../derivative-underlying-asset.model';
import { DerivativeUnderlyingAssetService } from '../service/derivative-underlying-asset.service';

import { DerivativeUnderlyingAssetRoutingResolveService } from './derivative-underlying-asset-routing-resolve.service';

describe('DerivativeUnderlyingAsset routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DerivativeUnderlyingAssetRoutingResolveService;
  let service: DerivativeUnderlyingAssetService;
  let resultDerivativeUnderlyingAsset: IDerivativeUnderlyingAsset | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DerivativeUnderlyingAssetRoutingResolveService);
    service = TestBed.inject(DerivativeUnderlyingAssetService);
    resultDerivativeUnderlyingAsset = undefined;
  });

  describe('resolve', () => {
    it('should return IDerivativeUnderlyingAsset returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeUnderlyingAsset = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDerivativeUnderlyingAsset).toEqual({ id: 123 });
    });

    it('should return new IDerivativeUnderlyingAsset if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeUnderlyingAsset = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDerivativeUnderlyingAsset).toEqual(new DerivativeUnderlyingAsset());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DerivativeUnderlyingAsset })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDerivativeUnderlyingAsset = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDerivativeUnderlyingAsset).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
