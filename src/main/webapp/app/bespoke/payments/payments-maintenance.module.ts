import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {
  PaymentDetailsMaintenanceModule,
} from "app/bespoke/payments/payment-details/payment-details-maintenance.module";
import {RouterModule, Routes} from "@angular/router";
import { SharedModule } from '../../shared/shared.module';


const routes: Routes = [
  {
    path: 'payment/details',
    loadChildren: () => import('./payment-details/payment-details-maintenance.module').then(m => m.PaymentDetailsMaintenanceModule),
  },
];

@NgModule({
  imports: [
    SharedModule,
    CommonModule,
    RouterModule.forChild(routes),
    PaymentDetailsMaintenanceModule
  ],
  exports: [
    PaymentDetailsMaintenanceModule
  ]
})
export class PaymentsMaintenanceModule {}
