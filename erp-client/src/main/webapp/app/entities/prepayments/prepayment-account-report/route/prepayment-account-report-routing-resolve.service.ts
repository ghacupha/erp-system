import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentAccountReport, PrepaymentAccountReport } from '../prepayment-account-report.model';
import { PrepaymentAccountReportService } from '../service/prepayment-account-report.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentAccountReportRoutingResolveService implements Resolve<IPrepaymentAccountReport> {
  constructor(protected service: PrepaymentAccountReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentAccountReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentAccountReport: HttpResponse<PrepaymentAccountReport>) => {
          if (prepaymentAccountReport.body) {
            return of(prepaymentAccountReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentAccountReport());
  }
}
