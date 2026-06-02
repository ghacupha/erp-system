jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouMonthlyDepreciationReportItem, RouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';
import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';

import { RouMonthlyDepreciationReportItemRoutingResolveService } from './rou-monthly-depreciation-report-item-routing-resolve.service';

describe('RouMonthlyDepreciationReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouMonthlyDepreciationReportItemRoutingResolveService;
  let service: RouMonthlyDepreciationReportItemService;
  let resultRouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouMonthlyDepreciationReportItemRoutingResolveService);
    service = TestBed.inject(RouMonthlyDepreciationReportItemService);
    resultRouMonthlyDepreciationReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouMonthlyDepreciationReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouMonthlyDepreciationReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouMonthlyDepreciationReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouMonthlyDepreciationReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouMonthlyDepreciationReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouMonthlyDepreciationReportItem).toEqual(new RouMonthlyDepreciationReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouMonthlyDepreciationReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouMonthlyDepreciationReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouMonthlyDepreciationReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
