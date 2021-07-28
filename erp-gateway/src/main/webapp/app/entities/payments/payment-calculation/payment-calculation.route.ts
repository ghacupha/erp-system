import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentCalculation, PaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';
import { PaymentCalculationService } from './payment-calculation.service';
import { PaymentCalculationComponent } from './payment-calculation.component';
import { PaymentCalculationDetailComponent } from './payment-calculation-detail.component';
import { PaymentCalculationUpdateComponent } from './payment-calculation-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentCalculationResolve implements Resolve<IPaymentCalculation> {
  constructor(private service: PaymentCalculationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentCalculation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentCalculation: HttpResponse<PaymentCalculation>) => {
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

export const paymentCalculationRoute: Routes = [
  {
    path: '',
    component: PaymentCalculationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'PaymentCalculations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentCalculationDetailComponent,
    resolve: {
      paymentCalculation: PaymentCalculationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentCalculations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentCalculationUpdateComponent,
    resolve: {
      paymentCalculation: PaymentCalculationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentCalculations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentCalculationUpdateComponent,
    resolve: {
      paymentCalculation: PaymentCalculationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentCalculations',
    },
    canActivate: [UserRouteAccessService],
  },
];
