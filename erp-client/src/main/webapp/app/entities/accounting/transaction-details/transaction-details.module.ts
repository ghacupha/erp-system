import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionDetailsComponent } from './list/transaction-details.component';
import { TransactionDetailsDetailComponent } from './detail/transaction-details-detail.component';
import { TransactionDetailsUpdateComponent } from './update/transaction-details-update.component';
import { TransactionDetailsDeleteDialogComponent } from './delete/transaction-details-delete-dialog.component';
import { TransactionDetailsRoutingModule } from './route/transaction-details-routing.module';

@NgModule({
  imports: [SharedModule, TransactionDetailsRoutingModule],
  declarations: [
    TransactionDetailsComponent,
    TransactionDetailsDetailComponent,
    TransactionDetailsUpdateComponent,
    TransactionDetailsDeleteDialogComponent,
  ],
  entryComponents: [TransactionDetailsDeleteDialogComponent],
})
export class TransactionDetailsModule {}
