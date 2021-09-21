import {NgModule} from "@angular/core";
import {SharedModule} from "../../shared/shared.module";
import {CommonModule} from "@angular/common";
import {ErpFilesNavigationComponent} from "./files/erp-files-navigation.component";
import {RouterModule} from "@angular/router";
import {PaymentsNavComponent} from "./payment-nav/payments-nav.component";

@NgModule({
  declarations: [
    ErpFilesNavigationComponent,
    PaymentsNavComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    RouterModule
  ],
  exports: [
    ErpFilesNavigationComponent,
    PaymentsNavComponent
  ]
})
export class ErpNavigationModule {}
