import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportStatus, ReportStatus } from '../report-status.model';
import { ReportStatusService } from '../service/report-status.service';

@Injectable({ providedIn: 'root' })
export class ReportStatusRoutingResolveService implements Resolve<IReportStatus> {
  constructor(protected service: ReportStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportStatus: HttpResponse<ReportStatus>) => {
          if (reportStatus.body) {
            return of(reportStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportStatus());
  }
}
