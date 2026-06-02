jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationPeriod, DepreciationPeriod } from '../depreciation-period.model';
import { DepreciationPeriodService } from '../service/depreciation-period.service';

import { DepreciationPeriodRoutingResolveService } from './depreciation-period-routing-resolve.service';

describe('DepreciationPeriod routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationPeriodRoutingResolveService;
  let service: DepreciationPeriodService;
  let resultDepreciationPeriod: IDepreciationPeriod | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationPeriodRoutingResolveService);
    service = TestBed.inject(DepreciationPeriodService);
    resultDepreciationPeriod = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationPeriod returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationPeriod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationPeriod).toEqual({ id: 123 });
    });

    it('should return new IDepreciationPeriod if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationPeriod = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationPeriod).toEqual(new DepreciationPeriod());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationPeriod })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationPeriod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationPeriod).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
