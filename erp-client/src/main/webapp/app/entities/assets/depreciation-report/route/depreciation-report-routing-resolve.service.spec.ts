jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepreciationReport, DepreciationReport } from '../depreciation-report.model';
import { DepreciationReportService } from '../service/depreciation-report.service';

import { DepreciationReportRoutingResolveService } from './depreciation-report-routing-resolve.service';

describe('DepreciationReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepreciationReportRoutingResolveService;
  let service: DepreciationReportService;
  let resultDepreciationReport: IDepreciationReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepreciationReportRoutingResolveService);
    service = TestBed.inject(DepreciationReportService);
    resultDepreciationReport = undefined;
  });

  describe('resolve', () => {
    it('should return IDepreciationReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationReport).toEqual({ id: 123 });
    });

    it('should return new IDepreciationReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepreciationReport).toEqual(new DepreciationReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepreciationReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepreciationReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepreciationReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
