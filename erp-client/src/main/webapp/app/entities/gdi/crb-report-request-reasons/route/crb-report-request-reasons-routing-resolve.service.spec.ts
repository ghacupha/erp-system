jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbReportRequestReasons, CrbReportRequestReasons } from '../crb-report-request-reasons.model';
import { CrbReportRequestReasonsService } from '../service/crb-report-request-reasons.service';

import { CrbReportRequestReasonsRoutingResolveService } from './crb-report-request-reasons-routing-resolve.service';

describe('CrbReportRequestReasons routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbReportRequestReasonsRoutingResolveService;
  let service: CrbReportRequestReasonsService;
  let resultCrbReportRequestReasons: ICrbReportRequestReasons | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbReportRequestReasonsRoutingResolveService);
    service = TestBed.inject(CrbReportRequestReasonsService);
    resultCrbReportRequestReasons = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbReportRequestReasons returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportRequestReasons = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbReportRequestReasons).toEqual({ id: 123 });
    });

    it('should return new ICrbReportRequestReasons if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportRequestReasons = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbReportRequestReasons).toEqual(new CrbReportRequestReasons());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbReportRequestReasons })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbReportRequestReasons = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbReportRequestReasons).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
