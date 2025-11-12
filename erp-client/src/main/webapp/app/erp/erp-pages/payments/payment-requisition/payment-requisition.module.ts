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
import { PaymentRequisitionComponent } from './list/payment-requisition.component';
import { PaymentRequisitionDetailComponent } from './detail/payment-requisition-detail.component';
import { PaymentRequisitionUpdateComponent } from './update/payment-requisition-update.component';
import { PaymentRequisitionDeleteDialogComponent } from './delete/payment-requisition-delete-dialog.component';
import { PaymentRequisitionRoutingModule } from './route/payment-requisition-routing.module';

@NgModule({
  imports: [SharedModule, PaymentRequisitionRoutingModule],
  declarations: [
    PaymentRequisitionComponent,
    PaymentRequisitionDetailComponent,
    PaymentRequisitionUpdateComponent,
    PaymentRequisitionDeleteDialogComponent,
  ],
  entryComponents: [PaymentRequisitionDeleteDialogComponent],
})
export class ErpServicePaymentRequisitionModule {}
