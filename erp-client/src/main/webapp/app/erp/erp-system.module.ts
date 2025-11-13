///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../shared/shared.module";
import {AboutErpSystemModule} from "./about/about-erp-system.module";
import {ErpNavbarModule} from "./navbar/erp-navbar.module";
import {ErpNavigationModule} from "./erp-nav/erp-navigation.module";
import {ErpPagesModule} from "./erp-pages/erp-pages.module";
import { ErpFormsModule } from './erp-forms/erp-forms.module';
import { ErpCommonModule } from './erp-common/erp-common.module';
import { ErpAssetsModule } from './erp-assets/erp-assets.module';
import { ErpTaxesModule } from './erp-taxes/erp-taxes.module';
import { ErpSettlementsModule } from './erp-settlements/erp-settlements.module';
import { ErpGranularModule } from './erp-granular/erp-granular.module';
import { ErpPrepaymentsAccountingModule } from './erp-prepayments/erp-prepayments-accounting.module';
import { ErpTransactionAccountModule } from './erp-accounts/erp-transaction-account.module';
import { ErpReportsModule } from './erp-reports/erp-reports.module';
import { ErpLeasesModule } from './erp-leases/erp-leases.module';
import { ErpGDIModule } from "./erp-gdi/erp-gdi.module";
import { ErpAdminModule } from './admin/erp-admin.module';
import { ErpAccountReportModule } from './erp-account-reports/erp-account-report.module';

export const routes: Routes = [];

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    AboutErpSystemModule,
    RouterModule.forChild(routes),
    ErpNavbarModule,
    ErpNavigationModule,
    ErpPagesModule,
    ErpFormsModule,
    ErpCommonModule,
    ErpAssetsModule,
    ErpTaxesModule,
    ErpReportsModule,
    ErpGranularModule,
    ErpSettlementsModule,
    ErpPrepaymentsAccountingModule,
    ErpTransactionAccountModule,
    ErpLeasesModule,
    ErpGDIModule,
    ErpAdminModule,
    ErpAccountReportModule,
  ],
  exports: [
    ErpNavbarModule,
    ErpPagesModule,
    ErpFormsModule,
    ErpAssetsModule,
    ErpTaxesModule,
    ErpLeasesModule,
    ErpReportsModule,
    ErpGranularModule,
    ErpSettlementsModule,
    ErpPrepaymentsAccountingModule,
    ErpTransactionAccountModule,
    ErpGDIModule,
    ErpAdminModule,
    ErpAccountReportModule,
  ]
})
export class ErpSystemModule {}
