jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWIPListReport, WIPListReport } from '../wip-list-report.model';
import { WIPListReportService } from '../service/wip-list-report.service';

import { WIPListReportRoutingResolveService } from './wip-list-report-routing-resolve.service';

describe('WIPListReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WIPListReportRoutingResolveService;
  let service: WIPListReportService;
  let resultWIPListReport: IWIPListReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(WIPListReportRoutingResolveService);
    service = TestBed.inject(WIPListReportService);
    resultWIPListReport = undefined;
  });

  describe('resolve', () => {
    it('should return IWIPListReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPListReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPListReport).toEqual({ id: 123 });
    });

    it('should return new IWIPListReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPListReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWIPListReport).toEqual(new WIPListReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WIPListReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPListReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPListReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
