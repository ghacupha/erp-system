import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeasePeriod, LeasePeriod } from '../lease-period.model';
import { LeasePeriodService } from '../service/lease-period.service';

@Injectable({ providedIn: 'root' })
export class LeasePeriodRoutingResolveService implements Resolve<ILeasePeriod> {
  constructor(protected service: LeasePeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeasePeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leasePeriod: HttpResponse<LeasePeriod>) => {
          if (leasePeriod.body) {
            return of(leasePeriod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeasePeriod());
  }
}
