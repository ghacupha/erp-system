jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityScheduleReport, LeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';
import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';

import { LeaseLiabilityScheduleReportRoutingResolveService } from './lease-liability-schedule-report-routing-resolve.service';

describe('LeaseLiabilityScheduleReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityScheduleReportRoutingResolveService;
  let service: LeaseLiabilityScheduleReportService;
  let resultLeaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityScheduleReportRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityScheduleReportService);
    resultLeaseLiabilityScheduleReport = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityScheduleReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleReport).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityScheduleReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityScheduleReport).toEqual(new LeaseLiabilityScheduleReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityScheduleReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
