import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";
import {PaymentMenuComponent} from "./payment-menu.component";
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [PaymentMenuComponent],
  imports: [CommonModule, SharedModule, RouterModule],
  exports: [PaymentMenuComponent]
})
export class BespokeNavigationModule{}
