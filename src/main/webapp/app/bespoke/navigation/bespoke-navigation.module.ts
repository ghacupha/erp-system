import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";
import {PaymentMenuComponent} from "./payments/payment-menu.component";
import {RouterModule} from "@angular/router";
import {MainMenuComponent} from "./main/main-menu.component";
import {BespokeMenusContainerComponent} from "./container/bespoke-menus-container.component";
import {FixedAssetsMenuComponent} from "./fixed-assets/fixed-assets-menu.component";

@NgModule({
  declarations: [
    PaymentMenuComponent,
    MainMenuComponent,
    BespokeMenusContainerComponent,
    FixedAssetsMenuComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule
  ],
  exports: [
    PaymentMenuComponent,
    MainMenuComponent,
    BespokeMenusContainerComponent,
    FixedAssetsMenuComponent
  ]
})
export class BespokeNavigationModule{}
