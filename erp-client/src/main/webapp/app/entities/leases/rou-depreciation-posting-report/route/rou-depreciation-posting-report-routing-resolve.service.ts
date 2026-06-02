import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationPostingReport, RouDepreciationPostingReport } from '../rou-depreciation-posting-report.model';
import { RouDepreciationPostingReportService } from '../service/rou-depreciation-posting-report.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationPostingReportRoutingResolveService implements Resolve<IRouDepreciationPostingReport> {
  constructor(protected service: RouDepreciationPostingReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationPostingReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationPostingReport: HttpResponse<RouDepreciationPostingReport>) => {
          if (rouDepreciationPostingReport.body) {
            return of(rouDepreciationPostingReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationPostingReport());
  }
}
