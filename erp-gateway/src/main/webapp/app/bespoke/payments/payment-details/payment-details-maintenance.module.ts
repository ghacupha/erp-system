import {Injectable, NgModule} from "@angular/core";
import {ErpGatewaySharedModule} from "app/shared/shared.module";
import {CommonModule} from "@angular/common";
import {ActivatedRouteSnapshot, Resolve, Router, RouterModule, Routes} from "@angular/router";
import {PaymentDetailsUpdateComponent} from "app/bespoke/payments/payment-details/payment-details-update.component";
import {Authority} from "app/shared/constants/authority.constants";
import {UserRouteAccessService} from "app/core/auth/user-route-access-service";
import {IPayment, Payment} from "app/shared/model/payments/payment.model";
import {PaymentService} from "app/entities/payments/payment/payment.service";
import {EMPTY, Observable, of} from "rxjs";
import {flatMap} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class PaymentDetailsResolve implements Resolve<IPayment> {
  constructor(private service: PaymentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPayment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((payment: HttpResponse<Payment>) => {
          if (payment.body) {
            return of(payment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Payment());
  }
}

export const paymentDetailsRoute: Routes = [
  {
    path: 'new',
    component: PaymentDetailsUpdateComponent,
    resolve: {
      payment: PaymentDetailsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Finance ERP | New Payment Details',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentDetailsUpdateComponent,
    resolve: {
      payment: PaymentDetailsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Finance ERP | Edit Payments Details',
    },
    canActivate: [UserRouteAccessService],
  },
]

@NgModule({
  declarations: [PaymentDetailsUpdateComponent],
  imports: [
    ErpGatewaySharedModule,
    CommonModule,
    RouterModule.forChild(paymentDetailsRoute)
  ],
})
export class PaymentDetailsMaintenanceModule{}
