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
