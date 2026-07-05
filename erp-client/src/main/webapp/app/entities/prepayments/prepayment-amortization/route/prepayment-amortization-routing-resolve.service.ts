import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentAmortization, PrepaymentAmortization } from '../prepayment-amortization.model';
import { PrepaymentAmortizationService } from '../service/prepayment-amortization.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentAmortizationRoutingResolveService implements Resolve<IPrepaymentAmortization> {
  constructor(protected service: PrepaymentAmortizationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentAmortization> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentAmortization: HttpResponse<PrepaymentAmortization>) => {
          if (prepaymentAmortization.body) {
            return of(prepaymentAmortization.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentAmortization());
  }
}
