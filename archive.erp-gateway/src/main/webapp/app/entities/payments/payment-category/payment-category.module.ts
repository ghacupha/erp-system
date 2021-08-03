import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { PaymentCategoryComponent } from './payment-category.component';
import { PaymentCategoryDetailComponent } from './payment-category-detail.component';
import { PaymentCategoryUpdateComponent } from './payment-category-update.component';
import { PaymentCategoryDeleteDialogComponent } from './payment-category-delete-dialog.component';
import { paymentCategoryRoute } from './payment-category.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(paymentCategoryRoute)],
  declarations: [
    PaymentCategoryComponent,
    PaymentCategoryDetailComponent,
    PaymentCategoryUpdateComponent,
    PaymentCategoryDeleteDialogComponent,
  ],
  entryComponents: [PaymentCategoryDeleteDialogComponent],
})
export class ErpServicePaymentCategoryModule {}
