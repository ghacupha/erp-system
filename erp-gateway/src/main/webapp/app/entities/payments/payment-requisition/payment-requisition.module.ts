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
