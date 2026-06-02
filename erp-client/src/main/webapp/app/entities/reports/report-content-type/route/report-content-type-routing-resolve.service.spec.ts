jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReportContentType, ReportContentType } from '../report-content-type.model';
import { ReportContentTypeService } from '../service/report-content-type.service';

import { ReportContentTypeRoutingResolveService } from './report-content-type-routing-resolve.service';

describe('ReportContentType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReportContentTypeRoutingResolveService;
  let service: ReportContentTypeService;
  let resultReportContentType: IReportContentType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ReportContentTypeRoutingResolveService);
    service = TestBed.inject(ReportContentTypeService);
    resultReportContentType = undefined;
  });

  describe('resolve', () => {
    it('should return IReportContentType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportContentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportContentType).toEqual({ id: 123 });
    });

    it('should return new IReportContentType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportContentType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReportContentType).toEqual(new ReportContentType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReportContentType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReportContentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReportContentType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
