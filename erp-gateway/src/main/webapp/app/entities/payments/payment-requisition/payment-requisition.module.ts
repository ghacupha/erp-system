///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { PaymentRequisitionComponent } from './payment-requisition.component';
import { PaymentRequisitionDetailComponent } from './payment-requisition-detail.component';
import { PaymentRequisitionUpdateComponent } from './payment-requisition-update.component';
import { PaymentRequisitionDeleteDialogComponent } from './payment-requisition-delete-dialog.component';
import { paymentRequisitionRoute } from './payment-requisition.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(paymentRequisitionRoute)],
  declarations: [
    PaymentRequisitionComponent,
    PaymentRequisitionDetailComponent,
    PaymentRequisitionUpdateComponent,
    PaymentRequisitionDeleteDialogComponent,
  ],
  entryComponents: [PaymentRequisitionDeleteDialogComponent],
})
export class ErpServicePaymentRequisitionModule {}
