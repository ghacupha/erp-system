import {NgModule} from "@angular/core";
import {SharedModule} from "../../shared/shared.module";
import {AboutComponent} from "./about.component";
import {RouterModule} from "@angular/router";
import {ABOUT_ROUTE} from "./about.route";
import {CommonModule} from "@angular/common";

@NgModule({
  declarations: [AboutComponent],
  imports: [
    SharedModule,
    CommonModule,
    RouterModule.forChild([ABOUT_ROUTE])
  ]
})
export class AboutErpSystemModule {}
