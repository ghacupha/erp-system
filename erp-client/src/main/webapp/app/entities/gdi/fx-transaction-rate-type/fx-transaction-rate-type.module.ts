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
import { FxTransactionRateTypeComponent } from './list/fx-transaction-rate-type.component';
import { FxTransactionRateTypeDetailComponent } from './detail/fx-transaction-rate-type-detail.component';
import { FxTransactionRateTypeUpdateComponent } from './update/fx-transaction-rate-type-update.component';
import { FxTransactionRateTypeDeleteDialogComponent } from './delete/fx-transaction-rate-type-delete-dialog.component';
import { FxTransactionRateTypeRoutingModule } from './route/fx-transaction-rate-type-routing.module';

@NgModule({
  imports: [SharedModule, FxTransactionRateTypeRoutingModule],
  declarations: [
    FxTransactionRateTypeComponent,
    FxTransactionRateTypeDetailComponent,
    FxTransactionRateTypeUpdateComponent,
    FxTransactionRateTypeDeleteDialogComponent,
  ],
  entryComponents: [FxTransactionRateTypeDeleteDialogComponent],
})
export class FxTransactionRateTypeModule {}
