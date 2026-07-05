jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityByAccountReport, LeaseLiabilityByAccountReport } from '../lease-liability-by-account-report.model';
import { LeaseLiabilityByAccountReportService } from '../service/lease-liability-by-account-report.service';

import { LeaseLiabilityByAccountReportRoutingResolveService } from './lease-liability-by-account-report-routing-resolve.service';

describe('LeaseLiabilityByAccountReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityByAccountReportRoutingResolveService;
  let service: LeaseLiabilityByAccountReportService;
  let resultLeaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityByAccountReportRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityByAccountReportService);
    resultLeaseLiabilityByAccountReport = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityByAccountReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityByAccountReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityByAccountReport).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityByAccountReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityByAccountReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityByAccountReport).toEqual(new LeaseLiabilityByAccountReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityByAccountReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityByAccountReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityByAccountReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
