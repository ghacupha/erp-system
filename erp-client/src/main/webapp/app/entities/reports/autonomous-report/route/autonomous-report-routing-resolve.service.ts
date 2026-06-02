import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAutonomousReport, AutonomousReport } from '../autonomous-report.model';
import { AutonomousReportService } from '../service/autonomous-report.service';

@Injectable({ providedIn: 'root' })
export class AutonomousReportRoutingResolveService implements Resolve<IAutonomousReport> {
  constructor(protected service: AutonomousReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAutonomousReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((autonomousReport: HttpResponse<AutonomousReport>) => {
          if (autonomousReport.body) {
            return of(autonomousReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AutonomousReport());
  }
}
