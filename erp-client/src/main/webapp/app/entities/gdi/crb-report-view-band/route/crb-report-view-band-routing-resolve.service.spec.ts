jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbReportViewBand, CrbReportViewBand } from '../crb-report-view-band.model';
import { CrbReportViewBandService } from '../service/crb-report-view-band.service';

import { CrbReportViewBandRoutingResolveService } from './crb-report-view-band-routing-resolve.service';

describe('CrbReportViewBand routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbReportViewBandRoutingResolveService;
  let service: CrbReportViewBandService;
  let resultCrbReportViewBand: ICrbReportViewBand | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbReportViewBandRoutingResolveService);
    service = TestBed.inject(CrbReportViewBandService);
    resultCrbReportViewBand = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbReportViewBand returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportViewBand = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbReportViewBand).toEqual({ id: 123 });
    });

    it('should return new ICrbReportViewBand if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportViewBand = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbReportViewBand).toEqual(new CrbReportViewBand());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbReportViewBand })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportViewBand = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbReportViewBand).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
