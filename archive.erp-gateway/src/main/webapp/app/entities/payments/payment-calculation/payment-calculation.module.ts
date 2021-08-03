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
