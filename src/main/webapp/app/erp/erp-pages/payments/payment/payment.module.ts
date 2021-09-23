import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentComponent } from './list/payment.component';
import { PaymentDetailComponent } from './detail/payment-detail.component';
import { PaymentUpdateComponent } from './update/payment-update.component';
import { PaymentDeleteDialogComponent } from './delete/payment-delete-dialog.component';
import { PaymentRoutingModule } from './route/payment-routing.module';
import {StoreModule} from "@ngrx/store";
import {paymentUpdateFormStateSelector, paymentUpdateStateReducer} from "../../../store/update-menu-status.reducer";

@NgModule({
  imports: [
    SharedModule,
    PaymentRoutingModule,
    StoreModule.forFeature(paymentUpdateFormStateSelector, paymentUpdateStateReducer),
  ],
  declarations: [PaymentComponent, PaymentDetailComponent, PaymentUpdateComponent, PaymentDeleteDialogComponent],
  entryComponents: [PaymentDeleteDialogComponent],
})
export class ErpServicePaymentModule {}
