import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentInvoice, PaymentInvoice } from '../payment-invoice.model';
import { PaymentInvoiceService } from '../service/payment-invoice.service';

@Injectable({ providedIn: 'root' })
export class PaymentInvoiceRoutingResolveService implements Resolve<IPaymentInvoice> {
  constructor(protected service: PaymentInvoiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentInvoice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentInvoice: HttpResponse<PaymentInvoice>) => {
          if (paymentInvoice.body) {
            return of(paymentInvoice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentInvoice());
  }
}
