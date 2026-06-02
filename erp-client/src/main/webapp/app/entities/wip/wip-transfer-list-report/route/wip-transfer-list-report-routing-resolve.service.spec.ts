jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWIPTransferListReport, WIPTransferListReport } from '../wip-transfer-list-report.model';
import { WIPTransferListReportService } from '../service/wip-transfer-list-report.service';

import { WIPTransferListReportRoutingResolveService } from './wip-transfer-list-report-routing-resolve.service';

describe('WIPTransferListReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WIPTransferListReportRoutingResolveService;
  let service: WIPTransferListReportService;
  let resultWIPTransferListReport: IWIPTransferListReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(WIPTransferListReportRoutingResolveService);
    service = TestBed.inject(WIPTransferListReportService);
    resultWIPTransferListReport = undefined;
  });

  describe('resolve', () => {
    it('should return IWIPTransferListReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPTransferListReport).toEqual({ id: 123 });
    });

    it('should return new IWIPTransferListReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWIPTransferListReport).toEqual(new WIPTransferListReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WIPTransferListReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWIPTransferListReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWIPTransferListReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
