import {NgModule} from "@angular/core";
import {ErpGatewaySharedModule} from "app/shared/shared.module";
import {CommonModule} from "@angular/common";
import {PaymentDetailsMaintenanceModule} from "app/bespoke/payments/payment-details/payment-details-maintenance.module";

@NgModule({
  imports: [ErpGatewaySharedModule, CommonModule, PaymentDetailsMaintenanceModule],
  exports: [PaymentDetailsMaintenanceModule]
})
export class PaymentsMaintenanceModule {}
