import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentReport, PrepaymentReport } from '../prepayment-report.model';
import { PrepaymentReportService } from '../service/prepayment-report.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentReportRoutingResolveService implements Resolve<IPrepaymentReport> {
  constructor(protected service: PrepaymentReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentReport: HttpResponse<PrepaymentReport>) => {
          if (prepaymentReport.body) {
            return of(prepaymentReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentReport());
  }
}
