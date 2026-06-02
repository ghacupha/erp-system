import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationEntryReportItem, RouDepreciationEntryReportItem } from '../rou-depreciation-entry-report-item.model';
import { RouDepreciationEntryReportItemService } from '../service/rou-depreciation-entry-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryReportItemRoutingResolveService implements Resolve<IRouDepreciationEntryReportItem> {
  constructor(protected service: RouDepreciationEntryReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationEntryReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationEntryReportItem: HttpResponse<RouDepreciationEntryReportItem>) => {
          if (rouDepreciationEntryReportItem.body) {
            return of(rouDepreciationEntryReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationEntryReportItem());
  }
}
