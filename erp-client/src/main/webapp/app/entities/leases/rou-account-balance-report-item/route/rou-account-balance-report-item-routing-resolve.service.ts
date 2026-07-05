import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAccountBalanceReportItem, RouAccountBalanceReportItem } from '../rou-account-balance-report-item.model';
import { RouAccountBalanceReportItemService } from '../service/rou-account-balance-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouAccountBalanceReportItemRoutingResolveService implements Resolve<IRouAccountBalanceReportItem> {
  constructor(protected service: RouAccountBalanceReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAccountBalanceReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAccountBalanceReportItem: HttpResponse<RouAccountBalanceReportItem>) => {
          if (rouAccountBalanceReportItem.body) {
            return of(rouAccountBalanceReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAccountBalanceReportItem());
  }
}
