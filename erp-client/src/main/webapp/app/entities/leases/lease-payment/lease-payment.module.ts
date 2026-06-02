import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeasePaymentComponent } from './list/lease-payment.component';
import { LeasePaymentDetailComponent } from './detail/lease-payment-detail.component';
import { LeasePaymentUpdateComponent } from './update/lease-payment-update.component';
import { LeasePaymentDeleteDialogComponent } from './delete/lease-payment-delete-dialog.component';
import { LeasePaymentRoutingModule } from './route/lease-payment-routing.module';

@NgModule({
  imports: [SharedModule, LeasePaymentRoutingModule],
  declarations: [LeasePaymentComponent, LeasePaymentDetailComponent, LeasePaymentUpdateComponent, LeasePaymentDeleteDialogComponent],
  entryComponents: [LeasePaymentDeleteDialogComponent],
})
export class LeasePaymentModule {}
