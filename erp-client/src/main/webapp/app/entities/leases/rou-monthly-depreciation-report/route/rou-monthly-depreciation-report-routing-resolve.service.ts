import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouMonthlyDepreciationReport, RouMonthlyDepreciationReport } from '../rou-monthly-depreciation-report.model';
import { RouMonthlyDepreciationReportService } from '../service/rou-monthly-depreciation-report.service';

@Injectable({ providedIn: 'root' })
export class RouMonthlyDepreciationReportRoutingResolveService implements Resolve<IRouMonthlyDepreciationReport> {
  constructor(protected service: RouMonthlyDepreciationReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouMonthlyDepreciationReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouMonthlyDepreciationReport: HttpResponse<RouMonthlyDepreciationReport>) => {
          if (rouMonthlyDepreciationReport.body) {
            return of(rouMonthlyDepreciationReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouMonthlyDepreciationReport());
  }
}
