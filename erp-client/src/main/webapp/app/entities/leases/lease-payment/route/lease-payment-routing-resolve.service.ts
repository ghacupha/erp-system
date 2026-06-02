import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeasePayment, LeasePayment } from '../lease-payment.model';
import { LeasePaymentService } from '../service/lease-payment.service';

@Injectable({ providedIn: 'root' })
export class LeasePaymentRoutingResolveService implements Resolve<ILeasePayment> {
  constructor(protected service: LeasePaymentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeasePayment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leasePayment: HttpResponse<LeasePayment>) => {
          if (leasePayment.body) {
            return of(leasePayment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeasePayment());
  }
}
