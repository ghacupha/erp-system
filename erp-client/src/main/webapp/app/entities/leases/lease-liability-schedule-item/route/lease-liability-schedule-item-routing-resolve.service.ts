import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityScheduleItem, LeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';
import { LeaseLiabilityScheduleItemService } from '../service/lease-liability-schedule-item.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleItemRoutingResolveService implements Resolve<ILeaseLiabilityScheduleItem> {
  constructor(protected service: LeaseLiabilityScheduleItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityScheduleItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityScheduleItem: HttpResponse<LeaseLiabilityScheduleItem>) => {
          if (leaseLiabilityScheduleItem.body) {
            return of(leaseLiabilityScheduleItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityScheduleItem());
  }
}
