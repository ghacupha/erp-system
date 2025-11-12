///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrepaymentOutstandingOverviewReport, PrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';
import { PrepaymentOutstandingOverviewReportService } from '../service/prepayment-outstanding-overview-report.service';

import { PrepaymentOutstandingOverviewReportRoutingResolveService } from './prepayment-outstanding-overview-report-routing-resolve.service';

describe('PrepaymentOutstandingOverviewReport routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PrepaymentOutstandingOverviewReportRoutingResolveService;
  let service: PrepaymentOutstandingOverviewReportService;
  let resultPrepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PrepaymentOutstandingOverviewReportRoutingResolveService);
    service = TestBed.inject(PrepaymentOutstandingOverviewReportService);
    resultPrepaymentOutstandingOverviewReport = undefined;
  });

  describe('resolve', () => {
    it('should return IPrepaymentOutstandingOverviewReport returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentOutstandingOverviewReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentOutstandingOverviewReport).toEqual({ id: 123 });
    });

    it('should return new IPrepaymentOutstandingOverviewReport if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentOutstandingOverviewReport = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPrepaymentOutstandingOverviewReport).toEqual(new PrepaymentOutstandingOverviewReport());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PrepaymentOutstandingOverviewReport })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrepaymentOutstandingOverviewReport = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrepaymentOutstandingOverviewReport).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
