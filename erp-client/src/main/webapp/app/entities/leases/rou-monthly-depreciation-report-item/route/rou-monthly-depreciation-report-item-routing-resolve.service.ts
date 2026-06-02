import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouMonthlyDepreciationReportItem, RouMonthlyDepreciationReportItem } from '../rou-monthly-depreciation-report-item.model';
import { RouMonthlyDepreciationReportItemService } from '../service/rou-monthly-depreciation-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouMonthlyDepreciationReportItemRoutingResolveService implements Resolve<IRouMonthlyDepreciationReportItem> {
  constructor(protected service: RouMonthlyDepreciationReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouMonthlyDepreciationReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouMonthlyDepreciationReportItem: HttpResponse<RouMonthlyDepreciationReportItem>) => {
          if (rouMonthlyDepreciationReportItem.body) {
            return of(rouMonthlyDepreciationReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouMonthlyDepreciationReportItem());
  }
}
