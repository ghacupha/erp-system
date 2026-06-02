import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentLabelComponent } from './list/payment-label.component';
import { PaymentLabelDetailComponent } from './detail/payment-label-detail.component';
import { PaymentLabelUpdateComponent } from './update/payment-label-update.component';
import { PaymentLabelDeleteDialogComponent } from './delete/payment-label-delete-dialog.component';
import { PaymentLabelRoutingModule } from './route/payment-label-routing.module';

@NgModule({
  imports: [SharedModule, PaymentLabelRoutingModule],
  declarations: [PaymentLabelComponent, PaymentLabelDetailComponent, PaymentLabelUpdateComponent, PaymentLabelDeleteDialogComponent],
  entryComponents: [PaymentLabelDeleteDialogComponent],
})
export class PaymentLabelModule {}
