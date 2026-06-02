import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkInProgressReport, WorkInProgressReport } from '../work-in-progress-report.model';
import { WorkInProgressReportService } from '../service/work-in-progress-report.service';

@Injectable({ providedIn: 'root' })
export class WorkInProgressReportRoutingResolveService implements Resolve<IWorkInProgressReport> {
  constructor(protected service: WorkInProgressReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkInProgressReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workInProgressReport: HttpResponse<WorkInProgressReport>) => {
          if (workInProgressReport.body) {
            return of(workInProgressReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkInProgressReport());
  }
}
