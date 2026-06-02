jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPdfReportRequisition, PdfReportRequisition } from '../pdf-report-requisition.model';
import { PdfReportRequisitionService } from '../service/pdf-report-requisition.service';

import { PdfReportRequisitionRoutingResolveService } from './pdf-report-requisition-routing-resolve.service';

describe('PdfReportRequisition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PdfReportRequisitionRoutingResolveService;
  let service: PdfReportRequisitionService;
  let resultPdfReportRequisition: IPdfReportRequisition | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PdfReportRequisitionRoutingResolveService);
    service = TestBed.inject(PdfReportRequisitionService);
    resultPdfReportRequisition = undefined;
  });

  describe('resolve', () => {
    it('should return IPdfReportRequisition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPdfReportRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPdfReportRequisition).toEqual({ id: 123 });
    });

    it('should return new IPdfReportRequisition if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPdfReportRequisition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPdfReportRequisition).toEqual(new PdfReportRequisition());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PdfReportRequisition })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPdfReportRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPdfReportRequisition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
