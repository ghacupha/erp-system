import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseAmortizationSchedule, LeaseAmortizationSchedule } from '../lease-amortization-schedule.model';
import { LeaseAmortizationScheduleService } from '../service/lease-amortization-schedule.service';

@Injectable({ providedIn: 'root' })
export class LeaseAmortizationScheduleRoutingResolveService implements Resolve<ILeaseAmortizationSchedule> {
  constructor(protected service: LeaseAmortizationScheduleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseAmortizationSchedule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseAmortizationSchedule: HttpResponse<LeaseAmortizationSchedule>) => {
          if (leaseAmortizationSchedule.body) {
            return of(leaseAmortizationSchedule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseAmortizationSchedule());
  }
}
