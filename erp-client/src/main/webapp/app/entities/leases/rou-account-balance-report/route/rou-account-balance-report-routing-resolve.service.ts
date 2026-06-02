import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAccountBalanceReport, RouAccountBalanceReport } from '../rou-account-balance-report.model';
import { RouAccountBalanceReportService } from '../service/rou-account-balance-report.service';

@Injectable({ providedIn: 'root' })
export class RouAccountBalanceReportRoutingResolveService implements Resolve<IRouAccountBalanceReport> {
  constructor(protected service: RouAccountBalanceReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAccountBalanceReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAccountBalanceReport: HttpResponse<RouAccountBalanceReport>) => {
          if (rouAccountBalanceReport.body) {
            return of(rouAccountBalanceReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAccountBalanceReport());
  }
}
