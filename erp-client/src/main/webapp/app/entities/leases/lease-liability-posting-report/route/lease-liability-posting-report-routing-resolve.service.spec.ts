jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityPostingReport, LeaseLiabilityPostingReport } from '../lease-liability-posting-report.model';
import { LeaseLiabilityPostingReportService } from '../service/lease-liability-posting-report.service';

import { LeaseLiabilityPostingReportRoutingResolveService } from './lease-liability-posting-report-routing-resolve.service';

describe('LeaseLiabilityPostingReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityPostingReportRoutingResolveService;
  let service: LeaseLiabilityPostingReportService;
  let resultLeaseLiabilityPostingReport: ILeaseLiabilityPostingReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityPostingReportRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityPostingReportService);
    resultLeaseLiabilityPostingReport = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityPostingReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityPostingReport).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityPostingReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityPostingReport).toEqual(new LeaseLiabilityPostingReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityPostingReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityPostingReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
