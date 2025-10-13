///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { FxTransactionTypeComponent } from './list/fx-transaction-type.component';
import { FxTransactionTypeDetailComponent } from './detail/fx-transaction-type-detail.component';
import { FxTransactionTypeUpdateComponent } from './update/fx-transaction-type-update.component';
import { FxTransactionTypeDeleteDialogComponent } from './delete/fx-transaction-type-delete-dialog.component';
import { FxTransactionTypeRoutingModule } from './route/fx-transaction-type-routing.module';

@NgModule({
  imports: [SharedModule, FxTransactionTypeRoutingModule],
  declarations: [
    FxTransactionTypeComponent,
    FxTransactionTypeDetailComponent,
    FxTransactionTypeUpdateComponent,
    FxTransactionTypeDeleteDialogComponent,
  ],
  entryComponents: [FxTransactionTypeDeleteDialogComponent],
})
export class FxTransactionTypeModule {}
