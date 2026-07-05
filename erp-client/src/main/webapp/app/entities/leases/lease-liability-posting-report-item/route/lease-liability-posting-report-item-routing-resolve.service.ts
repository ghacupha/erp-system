import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityPostingReportItem, LeaseLiabilityPostingReportItem } from '../lease-liability-posting-report-item.model';
import { LeaseLiabilityPostingReportItemService } from '../service/lease-liability-posting-report-item.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityPostingReportItemRoutingResolveService implements Resolve<ILeaseLiabilityPostingReportItem> {
  constructor(protected service: LeaseLiabilityPostingReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityPostingReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityPostingReportItem: HttpResponse<LeaseLiabilityPostingReportItem>) => {
          if (leaseLiabilityPostingReportItem.body) {
            return of(leaseLiabilityPostingReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityPostingReportItem());
  }
}
