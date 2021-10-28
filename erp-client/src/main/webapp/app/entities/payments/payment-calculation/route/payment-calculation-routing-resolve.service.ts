import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentCalculation, PaymentCalculation } from '../payment-calculation.model';
import { PaymentCalculationService } from '../service/payment-calculation.service';

@Injectable({ providedIn: 'root' })
export class PaymentCalculationRoutingResolveService implements Resolve<IPaymentCalculation> {
  constructor(protected service: PaymentCalculationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentCalculation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentCalculation: HttpResponse<PaymentCalculation>) => {
          if (paymentCalculation.body) {
            return of(paymentCalculation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentCalculation());
  }
}
