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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChartOfAccountsCodeComponent } from './list/chart-of-accounts-code.component';
import { ChartOfAccountsCodeDetailComponent } from './detail/chart-of-accounts-code-detail.component';
import { ChartOfAccountsCodeUpdateComponent } from './update/chart-of-accounts-code-update.component';
import { ChartOfAccountsCodeDeleteDialogComponent } from './delete/chart-of-accounts-code-delete-dialog.component';
import { ChartOfAccountsCodeRoutingModule } from './route/chart-of-accounts-code-routing.module';

@NgModule({
  imports: [SharedModule, ChartOfAccountsCodeRoutingModule],
  declarations: [
    ChartOfAccountsCodeComponent,
    ChartOfAccountsCodeDetailComponent,
    ChartOfAccountsCodeUpdateComponent,
    ChartOfAccountsCodeDeleteDialogComponent,
  ],
  entryComponents: [ChartOfAccountsCodeDeleteDialogComponent],
})
export class ChartOfAccountsCodeModule {}
