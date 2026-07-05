jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReportStatus, ReportStatus } from '../report-status.model';
import { ReportStatusService } from '../service/report-status.service';

import { ReportStatusRoutingResolveService } from './report-status-routing-resolve.service';

describe('ReportStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReportStatusRoutingResolveService;
  let service: ReportStatusService;
  let resultReportStatus: IReportStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ReportStatusRoutingResolveService);
    service = TestBed.inject(ReportStatusService);
    resultReportStatus = undefined;
  });

  describe('resolve', () => {
    it('should return IReportStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportStatus).toEqual({ id: 123 });
    });

    it('should return new IReportStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportStatus).toEqual(new ReportStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReportStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
