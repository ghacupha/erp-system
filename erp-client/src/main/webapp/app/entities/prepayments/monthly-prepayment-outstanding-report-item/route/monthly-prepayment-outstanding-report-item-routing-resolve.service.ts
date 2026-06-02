import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import {
  IMonthlyPrepaymentOutstandingReportItem,
  MonthlyPrepaymentOutstandingReportItem,
} from '../monthly-prepayment-outstanding-report-item.model';
import { MonthlyPrepaymentOutstandingReportItemService } from '../service/monthly-prepayment-outstanding-report-item.service';

@Injectable({ providedIn: 'root' })
export class MonthlyPrepaymentOutstandingReportItemRoutingResolveService implements Resolve<IMonthlyPrepaymentOutstandingReportItem> {
  constructor(protected service: MonthlyPrepaymentOutstandingReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMonthlyPrepaymentOutstandingReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((monthlyPrepaymentOutstandingReportItem: HttpResponse<MonthlyPrepaymentOutstandingReportItem>) => {
          if (monthlyPrepaymentOutstandingReportItem.body) {
            return of(monthlyPrepaymentOutstandingReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MonthlyPrepaymentOutstandingReportItem());
  }
}
