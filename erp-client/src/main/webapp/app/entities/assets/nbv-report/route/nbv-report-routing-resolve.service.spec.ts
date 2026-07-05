jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INbvReport, NbvReport } from '../nbv-report.model';
import { NbvReportService } from '../service/nbv-report.service';

import { NbvReportRoutingResolveService } from './nbv-report-routing-resolve.service';

describe('NbvReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NbvReportRoutingResolveService;
  let service: NbvReportService;
  let resultNbvReport: INbvReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NbvReportRoutingResolveService);
    service = TestBed.inject(NbvReportService);
    resultNbvReport = undefined;
  });

  describe('resolve', () => {
    it('should return INbvReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNbvReport).toEqual({ id: 123 });
    });

    it('should return new INbvReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNbvReport).toEqual(new NbvReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NbvReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNbvReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNbvReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
