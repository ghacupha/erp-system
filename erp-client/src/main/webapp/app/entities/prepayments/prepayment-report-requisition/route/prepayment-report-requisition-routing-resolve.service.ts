import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentReportRequisition, PrepaymentReportRequisition } from '../prepayment-report-requisition.model';
import { PrepaymentReportRequisitionService } from '../service/prepayment-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentReportRequisitionRoutingResolveService implements Resolve<IPrepaymentReportRequisition> {
  constructor(protected service: PrepaymentReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentReportRequisition: HttpResponse<PrepaymentReportRequisition>) => {
          if (prepaymentReportRequisition.body) {
            return of(prepaymentReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentReportRequisition());
  }
}
