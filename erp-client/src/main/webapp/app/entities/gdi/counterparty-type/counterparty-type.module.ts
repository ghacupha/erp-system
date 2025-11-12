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
import { CounterpartyTypeComponent } from './list/counterparty-type.component';
import { CounterpartyTypeDetailComponent } from './detail/counterparty-type-detail.component';
import { CounterpartyTypeUpdateComponent } from './update/counterparty-type-update.component';
import { CounterpartyTypeDeleteDialogComponent } from './delete/counterparty-type-delete-dialog.component';
import { CounterpartyTypeRoutingModule } from './route/counterparty-type-routing.module';

@NgModule({
  imports: [SharedModule, CounterpartyTypeRoutingModule],
  declarations: [
    CounterpartyTypeComponent,
    CounterpartyTypeDetailComponent,
    CounterpartyTypeUpdateComponent,
    CounterpartyTypeDeleteDialogComponent,
  ],
  entryComponents: [CounterpartyTypeDeleteDialogComponent],
})
export class CounterpartyTypeModule {}
