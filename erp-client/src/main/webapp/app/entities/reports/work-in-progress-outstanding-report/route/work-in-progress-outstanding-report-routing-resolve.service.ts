import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkInProgressOutstandingReport, WorkInProgressOutstandingReport } from '../work-in-progress-outstanding-report.model';
import { WorkInProgressOutstandingReportService } from '../service/work-in-progress-outstanding-report.service';

@Injectable({ providedIn: 'root' })
export class WorkInProgressOutstandingReportRoutingResolveService implements Resolve<IWorkInProgressOutstandingReport> {
  constructor(protected service: WorkInProgressOutstandingReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkInProgressOutstandingReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workInProgressOutstandingReport: HttpResponse<WorkInProgressOutstandingReport>) => {
          if (workInProgressOutstandingReport.body) {
            return of(workInProgressOutstandingReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkInProgressOutstandingReport());
  }
}
