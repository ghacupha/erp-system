import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityScheduleReportItem, LeaseLiabilityScheduleReportItem } from '../lease-liability-schedule-report-item.model';
import { LeaseLiabilityScheduleReportItemService } from '../service/lease-liability-schedule-report-item.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleReportItemRoutingResolveService implements Resolve<ILeaseLiabilityScheduleReportItem> {
  constructor(protected service: LeaseLiabilityScheduleReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityScheduleReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityScheduleReportItem: HttpResponse<LeaseLiabilityScheduleReportItem>) => {
          if (leaseLiabilityScheduleReportItem.body) {
            return of(leaseLiabilityScheduleReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityScheduleReportItem());
  }
}
