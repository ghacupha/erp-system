import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentRequisition, PaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';
import { PaymentRequisitionService } from './payment-requisition.service';
import { PaymentRequisitionComponent } from './payment-requisition.component';
import { PaymentRequisitionDetailComponent } from './payment-requisition-detail.component';
import { PaymentRequisitionUpdateComponent } from './payment-requisition-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentRequisitionResolve implements Resolve<IPaymentRequisition> {
  constructor(private service: PaymentRequisitionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentRequisition: HttpResponse<PaymentRequisition>) => {
          if (paymentRequisition.body) {
            return of(paymentRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentRequisition());
  }
}

export const paymentRequisitionRoute: Routes = [
  {
    path: '',
    component: PaymentRequisitionComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'PaymentRequisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentRequisitionDetailComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentRequisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentRequisitionUpdateComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentRequisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentRequisitionUpdateComponent,
    resolve: {
      paymentRequisition: PaymentRequisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'PaymentRequisitions',
    },
    canActivate: [UserRouteAccessService],
  },
];
