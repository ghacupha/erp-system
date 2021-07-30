import {NgModule} from "@angular/core";
import {ErpGatewaySharedModule} from "app/shared/shared.module";
import {CommonModule} from "@angular/common";
import {
  PaymentDetailsMaintenanceModule,
} from "app/bespoke/payments/payment-details/payment-details-maintenance.module";
import {RouterModule, Routes} from "@angular/router";


const routes: Routes = [
  {
    path: 'payment/details',
    loadChildren: () => import('./payment-details/payment-details-maintenance.module').then(m => m.PaymentDetailsMaintenanceModule),
  },
];

@NgModule({
  imports: [
    ErpGatewaySharedModule,
    CommonModule,
    RouterModule.forChild(routes),
    PaymentDetailsMaintenanceModule
  ],
  exports: [
    PaymentDetailsMaintenanceModule
  ]
})
export class PaymentsMaintenanceModule {}
