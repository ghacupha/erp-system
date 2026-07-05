import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import {
  IPrepaymentByAccountReportRequisition,
  PrepaymentByAccountReportRequisition,
} from '../prepayment-by-account-report-requisition.model';
import { PrepaymentByAccountReportRequisitionService } from '../service/prepayment-by-account-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentByAccountReportRequisitionRoutingResolveService implements Resolve<IPrepaymentByAccountReportRequisition> {
  constructor(protected service: PrepaymentByAccountReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentByAccountReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentByAccountReportRequisition: HttpResponse<PrepaymentByAccountReportRequisition>) => {
          if (prepaymentByAccountReportRequisition.body) {
            return of(prepaymentByAccountReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentByAccountReportRequisition());
  }
}
