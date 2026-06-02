import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationReport, DepreciationReport } from '../depreciation-report.model';
import { DepreciationReportService } from '../service/depreciation-report.service';

@Injectable({ providedIn: 'root' })
export class DepreciationReportRoutingResolveService implements Resolve<IDepreciationReport> {
  constructor(protected service: DepreciationReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationReport: HttpResponse<DepreciationReport>) => {
          if (depreciationReport.body) {
            return of(depreciationReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationReport());
  }
}
