import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentCalculationComponent } from './list/payment-calculation.component';
import { PaymentCalculationDetailComponent } from './detail/payment-calculation-detail.component';
import { PaymentCalculationUpdateComponent } from './update/payment-calculation-update.component';
import { PaymentCalculationDeleteDialogComponent } from './delete/payment-calculation-delete-dialog.component';
import { PaymentCalculationRoutingModule } from './route/payment-calculation-routing.module';

@NgModule({
  imports: [SharedModule, PaymentCalculationRoutingModule],
  declarations: [
    PaymentCalculationComponent,
    PaymentCalculationDetailComponent,
    PaymentCalculationUpdateComponent,
    PaymentCalculationDeleteDialogComponent,
  ],
  entryComponents: [PaymentCalculationDeleteDialogComponent],
})
export class ErpServicePaymentCalculationModule {}
