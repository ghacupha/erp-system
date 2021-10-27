import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SignedPaymentComponent } from './list/signed-payment.component';
import { SignedPaymentDetailComponent } from './detail/signed-payment-detail.component';
import { SignedPaymentUpdateComponent } from './update/signed-payment-update.component';
import { SignedPaymentDeleteDialogComponent } from './delete/signed-payment-delete-dialog.component';
import { SignedPaymentRoutingModule } from './route/signed-payment-routing.module';

@NgModule({
  imports: [SharedModule, SignedPaymentRoutingModule],
  declarations: [SignedPaymentComponent, SignedPaymentDetailComponent, SignedPaymentUpdateComponent, SignedPaymentDeleteDialogComponent],
  entryComponents: [SignedPaymentDeleteDialogComponent],
})
export class SignedPaymentModule {}
