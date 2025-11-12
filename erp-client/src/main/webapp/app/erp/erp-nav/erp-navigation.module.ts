///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import {SharedModule} from "../../shared/shared.module";
import {CommonModule} from "@angular/common";
import {ErpFilesNavigationComponent} from "./files/erp-files-navigation.component";
import {RouterModule} from "@angular/router";
import {PaymentsNavComponent} from "./payment-nav/payments-nav.component";
import {AssetsNavComponent} from "./assets-nav/assets-nav.component";
import { TaxesNavComponent } from './taxes-nav/taxes-nav.component';
import { GranularDataNavComponent } from './granular-data-nav/granular-data-nav.component';
import { PrepaymentsNavComponent } from './prepayments/prepayments-nav.component';
import { ReportsNavComponent } from './reports-nav/reports-nav.component';
import { SystemAdminNavComponent } from './system-admin-nav/system-admin-nav.component';
import { LeasesNavComponent } from './leases-nav/leases-nav.component';
import { TablesNavComponent } from './tables-nav/tables-nav.component';
import { ErpMaintenanceNavComponent } from './erp-maintenance/erp-maintenance-nav.component';
import { GdiNavComponent } from './gdi-nav/gdi-nav.component';
import { GdiDataNavComponent } from './gdi-nav/gdi-data-nav.component';
import { LeaseReportsNavComponent } from "./lease-reports-nav/lease-reports-nav.component";
import { ROUReportsNavComponent } from './lease-reports-nav/rou-reports-nav.component';
import { AccountsNavComponent } from './accounts-nav/accounts-nav.component';

@NgModule({
  declarations: [
    AssetsNavComponent,
    ErpFilesNavigationComponent,
    ErpMaintenanceNavComponent,
    GdiNavComponent,
    GdiDataNavComponent,
    GranularDataNavComponent,
    LeaseReportsNavComponent,
    LeasesNavComponent,
    PaymentsNavComponent,
    PrepaymentsNavComponent,
    ReportsNavComponent,
    ROUReportsNavComponent,
    SystemAdminNavComponent,
    TablesNavComponent,
    TaxesNavComponent,
    AccountsNavComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    RouterModule,
  ],
  exports: [
    ErpFilesNavigationComponent,
    PaymentsNavComponent,
    AssetsNavComponent,
    LeaseReportsNavComponent,
    TaxesNavComponent,
    GranularDataNavComponent,
    PrepaymentsNavComponent,
    ReportsNavComponent,
    SystemAdminNavComponent,
    LeasesNavComponent,
    TablesNavComponent,
    ROUReportsNavComponent,
    ErpMaintenanceNavComponent,
    GdiNavComponent,
    GdiDataNavComponent,
    AccountsNavComponent,
  ]
})
export class ErpNavigationModule {}
