import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityByAccountReportItem, LeaseLiabilityByAccountReportItem } from '../lease-liability-by-account-report-item.model';
import { LeaseLiabilityByAccountReportItemService } from '../service/lease-liability-by-account-report-item.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityByAccountReportItemRoutingResolveService implements Resolve<ILeaseLiabilityByAccountReportItem> {
  constructor(protected service: LeaseLiabilityByAccountReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityByAccountReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityByAccountReportItem: HttpResponse<LeaseLiabilityByAccountReportItem>) => {
          if (leaseLiabilityByAccountReportItem.body) {
            return of(leaseLiabilityByAccountReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityByAccountReportItem());
  }
}
