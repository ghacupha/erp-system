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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationPeriodComponent } from './list/depreciation-period.component';
import { DepreciationPeriodDetailComponent } from './detail/depreciation-period-detail.component';
import { DepreciationPeriodUpdateComponent } from './update/depreciation-period-update.component';
import { DepreciationPeriodDeleteDialogComponent } from './delete/depreciation-period-delete-dialog.component';
import { DepreciationPeriodRoutingModule } from './route/depreciation-period-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationPeriodRoutingModule],
  declarations: [
    DepreciationPeriodComponent,
    DepreciationPeriodDetailComponent,
    DepreciationPeriodUpdateComponent,
    DepreciationPeriodDeleteDialogComponent,
  ],
  entryComponents: [DepreciationPeriodDeleteDialogComponent],
})
export class DepreciationPeriodModule {}
