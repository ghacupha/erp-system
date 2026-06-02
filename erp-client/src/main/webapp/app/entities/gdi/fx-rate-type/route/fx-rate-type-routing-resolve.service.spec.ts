jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFxRateType, FxRateType } from '../fx-rate-type.model';
import { FxRateTypeService } from '../service/fx-rate-type.service';

import { FxRateTypeRoutingResolveService } from './fx-rate-type-routing-resolve.service';

describe('FxRateType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FxRateTypeRoutingResolveService;
  let service: FxRateTypeService;
  let resultFxRateType: IFxRateType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FxRateTypeRoutingResolveService);
    service = TestBed.inject(FxRateTypeService);
    resultFxRateType = undefined;
  });

  describe('resolve', () => {
    it('should return IFxRateType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxRateType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxRateType).toEqual({ id: 123 });
    });

    it('should return new IFxRateType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxRateType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFxRateType).toEqual(new FxRateType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FxRateType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFxRateType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFxRateType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
