jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReportDesign, ReportDesign } from '../report-design.model';
import { ReportDesignService } from '../service/report-design.service';

import { ReportDesignRoutingResolveService } from './report-design-routing-resolve.service';

describe('ReportDesign routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReportDesignRoutingResolveService;
  let service: ReportDesignService;
  let resultReportDesign: IReportDesign | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ReportDesignRoutingResolveService);
    service = TestBed.inject(ReportDesignService);
    resultReportDesign = undefined;
  });

  describe('resolve', () => {
    it('should return IReportDesign returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportDesign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportDesign).toEqual({ id: 123 });
    });

    it('should return new IReportDesign if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportDesign = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportDesign).toEqual(new ReportDesign());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReportDesign })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportDesign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportDesign).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
