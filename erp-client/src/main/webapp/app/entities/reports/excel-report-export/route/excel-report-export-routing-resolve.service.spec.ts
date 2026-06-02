jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExcelReportExport, ExcelReportExport } from '../excel-report-export.model';
import { ExcelReportExportService } from '../service/excel-report-export.service';

import { ExcelReportExportRoutingResolveService } from './excel-report-export-routing-resolve.service';

describe('ExcelReportExport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ExcelReportExportRoutingResolveService;
  let service: ExcelReportExportService;
  let resultExcelReportExport: IExcelReportExport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ExcelReportExportRoutingResolveService);
    service = TestBed.inject(ExcelReportExportService);
    resultExcelReportExport = undefined;
  });

  describe('resolve', () => {
    it('should return IExcelReportExport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExcelReportExport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExcelReportExport).toEqual({ id: 123 });
    });

    it('should return new IExcelReportExport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExcelReportExport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExcelReportExport).toEqual(new ExcelReportExport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExcelReportExport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExcelReportExport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExcelReportExport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
