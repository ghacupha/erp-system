import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CardAcquiringTransactionComponent } from './list/card-acquiring-transaction.component';
import { CardAcquiringTransactionDetailComponent } from './detail/card-acquiring-transaction-detail.component';
import { CardAcquiringTransactionUpdateComponent } from './update/card-acquiring-transaction-update.component';
import { CardAcquiringTransactionDeleteDialogComponent } from './delete/card-acquiring-transaction-delete-dialog.component';
import { CardAcquiringTransactionRoutingModule } from './route/card-acquiring-transaction-routing.module';

@NgModule({
  imports: [SharedModule, CardAcquiringTransactionRoutingModule],
  declarations: [
    CardAcquiringTransactionComponent,
    CardAcquiringTransactionDetailComponent,
    CardAcquiringTransactionUpdateComponent,
    CardAcquiringTransactionDeleteDialogComponent,
  ],
  entryComponents: [CardAcquiringTransactionDeleteDialogComponent],
})
export class CardAcquiringTransactionModule {}
