import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonthlyPrepaymentReportRequisition, MonthlyPrepaymentReportRequisition } from '../monthly-prepayment-report-requisition.model';
import { MonthlyPrepaymentReportRequisitionService } from '../service/monthly-prepayment-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class MonthlyPrepaymentReportRequisitionRoutingResolveService implements Resolve<IMonthlyPrepaymentReportRequisition> {
  constructor(protected service: MonthlyPrepaymentReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMonthlyPrepaymentReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((monthlyPrepaymentReportRequisition: HttpResponse<MonthlyPrepaymentReportRequisition>) => {
          if (monthlyPrepaymentReportRequisition.body) {
            return of(monthlyPrepaymentReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MonthlyPrepaymentReportRequisition());
  }
}
