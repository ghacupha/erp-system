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

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentOutstandingOverviewReport, PrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';
import { PrepaymentOutstandingOverviewReportService } from '../service/prepayment-outstanding-overview-report.service';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '../../../../config/input.constants';

@Injectable({ providedIn: 'root' })
export class PrepaymentOutstandingOverviewReportRoutingResolveService implements Resolve<IPrepaymentOutstandingOverviewReport> {
  constructor(protected service: PrepaymentOutstandingOverviewReportService, protected router: Router) {}

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentOutstandingOverviewReport> | Observable<never> {

    const reportDate: string = dayjs().format(DATE_FORMAT)

    if (reportDate) {
      return this.service.findByDate(reportDate).pipe(
        mergeMap((prepaymentOutstandingOverviewReport: HttpResponse<PrepaymentOutstandingOverviewReport>) => {
          if (prepaymentOutstandingOverviewReport.body) {
            return of(prepaymentOutstandingOverviewReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentOutstandingOverviewReport());
  }
}
