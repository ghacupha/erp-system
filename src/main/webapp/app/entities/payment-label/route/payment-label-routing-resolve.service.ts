import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentLabel, PaymentLabel } from '../payment-label.model';
import { PaymentLabelService } from '../service/payment-label.service';

@Injectable({ providedIn: 'root' })
export class PaymentLabelRoutingResolveService implements Resolve<IPaymentLabel> {
  constructor(protected service: PaymentLabelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentLabel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentLabel: HttpResponse<PaymentLabel>) => {
          if (paymentLabel.body) {
            return of(paymentLabel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentLabel());
  }
}
