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
import { CardAcquiringTransactionComponent } from './list/card-acquiring-transaction.component';
import { CardAcquiringTransactionDetailComponent } from './detail/card-acquiring-transaction-detail.component';
import { CardAcquiringTransactionUpdateComponent } from './update/card-acquiring-transaction-update.component';
import { CardAcquiringTransactionDeleteDialogComponent } from './delete/card-acquiring-transaction-delete-dialog.component';
import { CardAcquiringTransactionRoutingModule } from './route/card-acquiring-transaction-routing.module';

@NgModule({
  imports: [SharedModule, CardAcquiringTransactionRoutingModule],
  declarations: [
    CardAcquiringTransactionComponent,
    CardAcquiringTransactionDetailComponent,
    CardAcquiringTransactionUpdateComponent,
    CardAcquiringTransactionDeleteDialogComponent,
  ],
  entryComponents: [CardAcquiringTransactionDeleteDialogComponent],
})
export class CardAcquiringTransactionModule {}
