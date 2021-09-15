import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentCategory, PaymentCategory } from '../payment-category.model';
import { PaymentCategoryService } from '../service/payment-category.service';

@Injectable({ providedIn: 'root' })
export class PaymentCategoryRoutingResolveService implements Resolve<IPaymentCategory> {
  constructor(protected service: PaymentCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentCategory: HttpResponse<PaymentCategory>) => {
          if (paymentCategory.body) {
            return of(paymentCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentCategory());
  }
}
