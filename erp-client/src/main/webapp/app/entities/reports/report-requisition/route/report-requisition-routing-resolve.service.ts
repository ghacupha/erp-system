import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportRequisition, ReportRequisition } from '../report-requisition.model';
import { ReportRequisitionService } from '../service/report-requisition.service';

@Injectable({ providedIn: 'root' })
export class ReportRequisitionRoutingResolveService implements Resolve<IReportRequisition> {
  constructor(protected service: ReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportRequisition: HttpResponse<ReportRequisition>) => {
          if (reportRequisition.body) {
            return of(reportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportRequisition());
  }
}
