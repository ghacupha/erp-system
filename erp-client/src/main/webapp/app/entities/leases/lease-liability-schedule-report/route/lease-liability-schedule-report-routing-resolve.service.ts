import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityScheduleReport, LeaseLiabilityScheduleReport } from '../lease-liability-schedule-report.model';
import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleReportRoutingResolveService implements Resolve<ILeaseLiabilityScheduleReport> {
  constructor(protected service: LeaseLiabilityScheduleReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityScheduleReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityScheduleReport: HttpResponse<LeaseLiabilityScheduleReport>) => {
          if (leaseLiabilityScheduleReport.body) {
            return of(leaseLiabilityScheduleReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityScheduleReport());
  }
}
