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
import { GdiTransactionDataIndexComponent } from './list/gdi-transaction-data-index.component';
import { GdiTransactionDataIndexDetailComponent } from './detail/gdi-transaction-data-index-detail.component';
import { GdiTransactionDataIndexUpdateComponent } from './update/gdi-transaction-data-index-update.component';
import { GdiTransactionDataIndexDeleteDialogComponent } from './delete/gdi-transaction-data-index-delete-dialog.component';
import { GdiTransactionDataIndexRoutingModule } from './route/gdi-transaction-data-index-routing.module';

@NgModule({
  imports: [SharedModule, GdiTransactionDataIndexRoutingModule],
  declarations: [
    GdiTransactionDataIndexComponent,
    GdiTransactionDataIndexDetailComponent,
    GdiTransactionDataIndexUpdateComponent,
    GdiTransactionDataIndexDeleteDialogComponent,
  ],
  entryComponents: [GdiTransactionDataIndexDeleteDialogComponent],
})
export class GdiTransactionDataIndexModule {}
