import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import {
  IWorkInProgressOutstandingReportRequisition,
  WorkInProgressOutstandingReportRequisition,
} from '../work-in-progress-outstanding-report-requisition.model';
import { WorkInProgressOutstandingReportRequisitionService } from '../service/work-in-progress-outstanding-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class WorkInProgressOutstandingReportRequisitionRoutingResolveService
  implements Resolve<IWorkInProgressOutstandingReportRequisition>
{
  constructor(protected service: WorkInProgressOutstandingReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkInProgressOutstandingReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workInProgressOutstandingReportRequisition: HttpResponse<WorkInProgressOutstandingReportRequisition>) => {
          if (workInProgressOutstandingReportRequisition.body) {
            return of(workInProgressOutstandingReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkInProgressOutstandingReportRequisition());
  }
}
