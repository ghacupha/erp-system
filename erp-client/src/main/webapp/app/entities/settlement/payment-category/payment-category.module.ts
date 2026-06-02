import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentCategoryComponent } from './list/payment-category.component';
import { PaymentCategoryDetailComponent } from './detail/payment-category-detail.component';
import { PaymentCategoryUpdateComponent } from './update/payment-category-update.component';
import { PaymentCategoryDeleteDialogComponent } from './delete/payment-category-delete-dialog.component';
import { PaymentCategoryRoutingModule } from './route/payment-category-routing.module';

@NgModule({
  imports: [SharedModule, PaymentCategoryRoutingModule],
  declarations: [
    PaymentCategoryComponent,
    PaymentCategoryDetailComponent,
    PaymentCategoryUpdateComponent,
    PaymentCategoryDeleteDialogComponent,
  ],
  entryComponents: [PaymentCategoryDeleteDialogComponent],
})
export class ErpServicePaymentCategoryModule {}
