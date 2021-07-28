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
import { PaymentCalculationComponent } from './payment-calculation.component';
import { PaymentCalculationDetailComponent } from './payment-calculation-detail.component';
import { PaymentCalculationUpdateComponent } from './payment-calculation-update.component';
import { PaymentCalculationDeleteDialogComponent } from './payment-calculation-delete-dialog.component';
import { paymentCalculationRoute } from './payment-calculation.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(paymentCalculationRoute)],
  declarations: [
    PaymentCalculationComponent,
    PaymentCalculationDetailComponent,
    PaymentCalculationUpdateComponent,
    PaymentCalculationDeleteDialogComponent,
  ],
  entryComponents: [PaymentCalculationDeleteDialogComponent],
})
export class ErpServicePaymentCalculationModule {}
