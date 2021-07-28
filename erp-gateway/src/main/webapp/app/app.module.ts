import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { ErpGatewayCoreModule } from 'app/core/core.module';
import { ErpGatewayAppRoutingModule } from './app-routing.module';
import { ErpGatewayHomeModule } from './home/home.module';
import { ErpGatewayEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { BespokeModule } from 'app/bespoke/bespoke.module';

@NgModule({
  imports: [
    BrowserModule,
    ErpGatewaySharedModule,
    ErpGatewayCoreModule,
    ErpGatewayHomeModule,
    BespokeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ErpGatewayEntityModule,
    ErpGatewayAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ErpGatewayAppModule {}
