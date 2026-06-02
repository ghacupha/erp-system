import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseRepaymentPeriod, LeaseRepaymentPeriod } from '../lease-repayment-period.model';
import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';

@Injectable({ providedIn: 'root' })
export class LeaseRepaymentPeriodRoutingResolveService implements Resolve<ILeaseRepaymentPeriod> {
  constructor(protected service: LeaseRepaymentPeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseRepaymentPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseRepaymentPeriod: HttpResponse<LeaseRepaymentPeriod>) => {
          if (leaseRepaymentPeriod.body) {
            return of(leaseRepaymentPeriod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseRepaymentPeriod());
  }
}
