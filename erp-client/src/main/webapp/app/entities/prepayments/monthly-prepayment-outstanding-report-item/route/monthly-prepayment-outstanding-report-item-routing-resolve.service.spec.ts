jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import {
  IMonthlyPrepaymentOutstandingReportItem,
  MonthlyPrepaymentOutstandingReportItem,
} from '../monthly-prepayment-outstanding-report-item.model';
import { MonthlyPrepaymentOutstandingReportItemService } from '../service/monthly-prepayment-outstanding-report-item.service';

import { MonthlyPrepaymentOutstandingReportItemRoutingResolveService } from './monthly-prepayment-outstanding-report-item-routing-resolve.service';

describe('MonthlyPrepaymentOutstandingReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MonthlyPrepaymentOutstandingReportItemRoutingResolveService;
  let service: MonthlyPrepaymentOutstandingReportItemService;
  let resultMonthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MonthlyPrepaymentOutstandingReportItemRoutingResolveService);
    service = TestBed.inject(MonthlyPrepaymentOutstandingReportItemService);
    resultMonthlyPrepaymentOutstandingReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IMonthlyPrepaymentOutstandingReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMonthlyPrepaymentOutstandingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMonthlyPrepaymentOutstandingReportItem).toEqual({ id: 123 });
    });

    it('should return new IMonthlyPrepaymentOutstandingReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMonthlyPrepaymentOutstandingReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMonthlyPrepaymentOutstandingReportItem).toEqual(new MonthlyPrepaymentOutstandingReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest
        .spyOn(service, 'find')
        .mockReturnValue(of(new HttpResponse({ body: null as unknown as MonthlyPrepaymentOutstandingReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMonthlyPrepaymentOutstandingReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMonthlyPrepaymentOutstandingReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
