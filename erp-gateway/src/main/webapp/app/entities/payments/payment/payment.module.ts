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
import { PaymentComponent } from './payment.component';
import { PaymentDetailComponent } from './payment-detail.component';
import { PaymentUpdateComponent } from './payment-update.component';
import { PaymentDeleteDialogComponent } from './payment-delete-dialog.component';
import { paymentRoute } from './payment.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(paymentRoute)],
  declarations: [PaymentComponent, PaymentDetailComponent, PaymentUpdateComponent, PaymentDeleteDialogComponent],
  entryComponents: [PaymentDeleteDialogComponent],
})
export class ErpServicePaymentModule {}
