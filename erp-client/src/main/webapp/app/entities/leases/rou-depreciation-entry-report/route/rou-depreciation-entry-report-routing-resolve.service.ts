import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationEntryReport, RouDepreciationEntryReport } from '../rou-depreciation-entry-report.model';
import { RouDepreciationEntryReportService } from '../service/rou-depreciation-entry-report.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryReportRoutingResolveService implements Resolve<IRouDepreciationEntryReport> {
  constructor(protected service: RouDepreciationEntryReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationEntryReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationEntryReport: HttpResponse<RouDepreciationEntryReport>) => {
          if (rouDepreciationEntryReport.body) {
            return of(rouDepreciationEntryReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationEntryReport());
  }
}
