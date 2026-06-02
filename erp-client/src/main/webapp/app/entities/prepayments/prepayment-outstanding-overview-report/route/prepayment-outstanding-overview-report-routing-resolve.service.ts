import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentOutstandingOverviewReport, PrepaymentOutstandingOverviewReport } from '../prepayment-outstanding-overview-report.model';
import { PrepaymentOutstandingOverviewReportService } from '../service/prepayment-outstanding-overview-report.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentOutstandingOverviewReportRoutingResolveService implements Resolve<IPrepaymentOutstandingOverviewReport> {
  constructor(protected service: PrepaymentOutstandingOverviewReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentOutstandingOverviewReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
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
