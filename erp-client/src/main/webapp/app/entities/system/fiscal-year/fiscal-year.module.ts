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
import { FiscalYearComponent } from './list/fiscal-year.component';
import { FiscalYearDetailComponent } from './detail/fiscal-year-detail.component';
import { FiscalYearUpdateComponent } from './update/fiscal-year-update.component';
import { FiscalYearDeleteDialogComponent } from './delete/fiscal-year-delete-dialog.component';
import { FiscalYearRoutingModule } from './route/fiscal-year-routing.module';

@NgModule({
  imports: [SharedModule, FiscalYearRoutingModule],
  declarations: [FiscalYearComponent, FiscalYearDetailComponent, FiscalYearUpdateComponent, FiscalYearDeleteDialogComponent],
  entryComponents: [FiscalYearDeleteDialogComponent],
})
export class FiscalYearModule {}
