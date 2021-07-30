import {NgModule} from "@angular/core";
import {ErpGatewaySharedModule} from "app/shared/shared.module";
import {CommonModule} from "@angular/common";
import {RouterModule, ROUTES} from "@angular/router";

@NgModule({
  imports: [
    ErpGatewaySharedModule,
    CommonModule,
    RouterModule.forChild([])
  ],
})
export class PaymentDetailsMaintenanceModule{}
