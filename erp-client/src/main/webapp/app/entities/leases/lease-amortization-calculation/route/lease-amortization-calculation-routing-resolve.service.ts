import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseAmortizationCalculation, LeaseAmortizationCalculation } from '../lease-amortization-calculation.model';
import { LeaseAmortizationCalculationService } from '../service/lease-amortization-calculation.service';

@Injectable({ providedIn: 'root' })
export class LeaseAmortizationCalculationRoutingResolveService implements Resolve<ILeaseAmortizationCalculation> {
  constructor(protected service: LeaseAmortizationCalculationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseAmortizationCalculation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseAmortizationCalculation: HttpResponse<LeaseAmortizationCalculation>) => {
          if (leaseAmortizationCalculation.body) {
            return of(leaseAmortizationCalculation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseAmortizationCalculation());
  }
}
