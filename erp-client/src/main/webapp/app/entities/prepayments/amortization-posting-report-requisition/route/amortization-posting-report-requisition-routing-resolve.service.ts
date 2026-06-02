import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import {
  IAmortizationPostingReportRequisition,
  AmortizationPostingReportRequisition,
} from '../amortization-posting-report-requisition.model';
import { AmortizationPostingReportRequisitionService } from '../service/amortization-posting-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class AmortizationPostingReportRequisitionRoutingResolveService implements Resolve<IAmortizationPostingReportRequisition> {
  constructor(protected service: AmortizationPostingReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmortizationPostingReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amortizationPostingReportRequisition: HttpResponse<AmortizationPostingReportRequisition>) => {
          if (amortizationPostingReportRequisition.body) {
            return of(amortizationPostingReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AmortizationPostingReportRequisition());
  }
}
