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
import { WeeklyCounterfeitHoldingComponent } from './list/weekly-counterfeit-holding.component';
import { WeeklyCounterfeitHoldingDetailComponent } from './detail/weekly-counterfeit-holding-detail.component';
import { WeeklyCounterfeitHoldingUpdateComponent } from './update/weekly-counterfeit-holding-update.component';
import { WeeklyCounterfeitHoldingDeleteDialogComponent } from './delete/weekly-counterfeit-holding-delete-dialog.component';
import { WeeklyCounterfeitHoldingRoutingModule } from './route/weekly-counterfeit-holding-routing.module';

@NgModule({
  imports: [SharedModule, WeeklyCounterfeitHoldingRoutingModule],
  declarations: [
    WeeklyCounterfeitHoldingComponent,
    WeeklyCounterfeitHoldingDetailComponent,
    WeeklyCounterfeitHoldingUpdateComponent,
    WeeklyCounterfeitHoldingDeleteDialogComponent,
  ],
  entryComponents: [WeeklyCounterfeitHoldingDeleteDialogComponent],
})
export class WeeklyCounterfeitHoldingModule {}
