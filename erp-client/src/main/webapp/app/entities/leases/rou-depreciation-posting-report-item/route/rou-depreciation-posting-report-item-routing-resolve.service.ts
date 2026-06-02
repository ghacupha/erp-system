import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationPostingReportItem, RouDepreciationPostingReportItem } from '../rou-depreciation-posting-report-item.model';
import { RouDepreciationPostingReportItemService } from '../service/rou-depreciation-posting-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationPostingReportItemRoutingResolveService implements Resolve<IRouDepreciationPostingReportItem> {
  constructor(protected service: RouDepreciationPostingReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationPostingReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationPostingReportItem: HttpResponse<RouDepreciationPostingReportItem>) => {
          if (rouDepreciationPostingReportItem.body) {
            return of(rouDepreciationPostingReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationPostingReportItem());
  }
}
