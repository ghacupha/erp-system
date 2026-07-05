jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouDepreciationPostingReport, RouDepreciationPostingReport } from '../rou-depreciation-posting-report.model';
import { RouDepreciationPostingReportService } from '../service/rou-depreciation-posting-report.service';

import { RouDepreciationPostingReportRoutingResolveService } from './rou-depreciation-posting-report-routing-resolve.service';

describe('RouDepreciationPostingReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouDepreciationPostingReportRoutingResolveService;
  let service: RouDepreciationPostingReportService;
  let resultRouDepreciationPostingReport: IRouDepreciationPostingReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouDepreciationPostingReportRoutingResolveService);
    service = TestBed.inject(RouDepreciationPostingReportService);
    resultRouDepreciationPostingReport = undefined;
  });

  describe('resolve', () => {
    it('should return IRouDepreciationPostingReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationPostingReport).toEqual({ id: 123 });
    });

    it('should return new IRouDepreciationPostingReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouDepreciationPostingReport).toEqual(new RouDepreciationPostingReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouDepreciationPostingReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouDepreciationPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouDepreciationPostingReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
