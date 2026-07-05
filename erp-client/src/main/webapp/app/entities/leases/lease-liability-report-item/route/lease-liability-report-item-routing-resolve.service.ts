import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityReportItem, LeaseLiabilityReportItem } from '../lease-liability-report-item.model';
import { LeaseLiabilityReportItemService } from '../service/lease-liability-report-item.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityReportItemRoutingResolveService implements Resolve<ILeaseLiabilityReportItem> {
  constructor(protected service: LeaseLiabilityReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityReportItem: HttpResponse<LeaseLiabilityReportItem>) => {
          if (leaseLiabilityReportItem.body) {
            return of(leaseLiabilityReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityReportItem());
  }
}
