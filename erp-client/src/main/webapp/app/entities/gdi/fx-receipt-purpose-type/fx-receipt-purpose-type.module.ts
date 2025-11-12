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
import { FxReceiptPurposeTypeComponent } from './list/fx-receipt-purpose-type.component';
import { FxReceiptPurposeTypeDetailComponent } from './detail/fx-receipt-purpose-type-detail.component';
import { FxReceiptPurposeTypeUpdateComponent } from './update/fx-receipt-purpose-type-update.component';
import { FxReceiptPurposeTypeDeleteDialogComponent } from './delete/fx-receipt-purpose-type-delete-dialog.component';
import { FxReceiptPurposeTypeRoutingModule } from './route/fx-receipt-purpose-type-routing.module';

@NgModule({
  imports: [SharedModule, FxReceiptPurposeTypeRoutingModule],
  declarations: [
    FxReceiptPurposeTypeComponent,
    FxReceiptPurposeTypeDetailComponent,
    FxReceiptPurposeTypeUpdateComponent,
    FxReceiptPurposeTypeDeleteDialogComponent,
  ],
  entryComponents: [FxReceiptPurposeTypeDeleteDialogComponent],
})
export class FxReceiptPurposeTypeModule {}
