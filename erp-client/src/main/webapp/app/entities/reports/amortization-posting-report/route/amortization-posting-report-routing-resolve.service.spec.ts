jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAmortizationPostingReport, AmortizationPostingReport } from '../amortization-posting-report.model';
import { AmortizationPostingReportService } from '../service/amortization-posting-report.service';

import { AmortizationPostingReportRoutingResolveService } from './amortization-posting-report-routing-resolve.service';

describe('AmortizationPostingReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AmortizationPostingReportRoutingResolveService;
  let service: AmortizationPostingReportService;
  let resultAmortizationPostingReport: IAmortizationPostingReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AmortizationPostingReportRoutingResolveService);
    service = TestBed.inject(AmortizationPostingReportService);
    resultAmortizationPostingReport = undefined;
  });

  describe('resolve', () => {
    it('should return IAmortizationPostingReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmortizationPostingReport).toEqual({ id: 123 });
    });

    it('should return new IAmortizationPostingReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationPostingReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAmortizationPostingReport).toEqual(new AmortizationPostingReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AmortizationPostingReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmortizationPostingReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmortizationPostingReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
