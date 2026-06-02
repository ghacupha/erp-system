jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICollateralType, CollateralType } from '../collateral-type.model';
import { CollateralTypeService } from '../service/collateral-type.service';

import { CollateralTypeRoutingResolveService } from './collateral-type-routing-resolve.service';

describe('CollateralType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CollateralTypeRoutingResolveService;
  let service: CollateralTypeService;
  let resultCollateralType: ICollateralType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CollateralTypeRoutingResolveService);
    service = TestBed.inject(CollateralTypeService);
    resultCollateralType = undefined;
  });

  describe('resolve', () => {
    it('should return ICollateralType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollateralType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCollateralType).toEqual({ id: 123 });
    });

    it('should return new ICollateralType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollateralType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCollateralType).toEqual(new CollateralType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CollateralType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCollateralType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCollateralType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
