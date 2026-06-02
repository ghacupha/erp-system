jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrepaymentReportRequisition, PrepaymentReportRequisition } from '../prepayment-report-requisition.model';
import { PrepaymentReportRequisitionService } from '../service/prepayment-report-requisition.service';

import { PrepaymentReportRequisitionRoutingResolveService } from './prepayment-report-requisition-routing-resolve.service';

describe('PrepaymentReportRequisition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PrepaymentReportRequisitionRoutingResolveService;
  let service: PrepaymentReportRequisitionService;
  let resultPrepaymentReportRequisition: IPrepaymentReportRequisition | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PrepaymentReportRequisitionRoutingResolveService);
    service = TestBed.inject(PrepaymentReportRequisitionService);
    resultPrepaymentReportRequisition = undefined;
  });

  describe('resolve', () => {
    it('should return IPrepaymentReportRequisition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentReportRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentReportRequisition).toEqual({ id: 123 });
    });

    it('should return new IPrepaymentReportRequisition if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentReportRequisition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPrepaymentReportRequisition).toEqual(new PrepaymentReportRequisition());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PrepaymentReportRequisition })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentReportRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentReportRequisition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
