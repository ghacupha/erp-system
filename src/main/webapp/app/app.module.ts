import {LOCALE_ID, NgModule} from '@angular/core';
import {registerLocaleData} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import locale from '@angular/common/locales/en';
import {BrowserModule, Title} from '@angular/platform-browser';
import {ServiceWorkerModule} from '@angular/service-worker';
import {FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {NgxWebstorageModule} from 'ngx-webstorage';
import * as dayjs from 'dayjs';
import {NgbDateAdapter, NgbDatepickerConfig} from '@ng-bootstrap/ng-bootstrap';

import {ApplicationConfigService} from 'app/core/config/application-config.service';
import './config/dayjs';
import {SharedModule} from 'app/shared/shared.module';
import {AppRoutingModule} from './app-routing.module';
import {HomeModule} from './home/home.module';
import {EntityRoutingModule} from './entities/entity-routing.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {NgbDateDayjsAdapter} from './config/datepicker-adapter';
import {fontAwesomeIcons} from './config/font-awesome-icons';
import {httpInterceptorProviders} from 'app/core/interceptor/index';
import {MainComponent} from './layouts/main/main.component';
import {NavbarComponent} from './layouts/navbar/navbar.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {PageRibbonComponent} from './layouts/profiles/page-ribbon.component';
import {ErrorComponent} from './layouts/error/error.component';
import {ErpSystemModule} from "./erp/erp-system.module";
import {NgSelectModule} from "@ng-select/ng-select";
import {ErpStoreModule} from "./erp/store/erp-store.module";
import {LoggerModule, NgxLoggerLevel} from "ngx-logger";

@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    HomeModule,
    ErpSystemModule,
    NgSelectModule,
    ErpStoreModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    EntityRoutingModule,
    AppRoutingModule,
    LoggerModule.forRoot({serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.DEBUG}),
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-', caseSensitive: true }),
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    httpInterceptorProviders,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
