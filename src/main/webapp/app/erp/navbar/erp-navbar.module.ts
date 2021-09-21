import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";
import {RouterModule} from "@angular/router";
import {ErpNavbarRoute} from "./erp-navbar.route";
import {ERPNavbarComponent} from "./erp-navbar.component";
import {ErpNavigationModule} from "../erp-nav/erp-navigation.module";

@NgModule({
  declarations: [
    ERPNavbarComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forRoot([ErpNavbarRoute]),
    ErpNavigationModule
  ],
  exports: [
    ERPNavbarComponent,
    RouterModule
  ]
})
export class ErpNavbarModule {}
