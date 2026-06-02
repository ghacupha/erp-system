import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionAccountLedgerComponent } from './list/transaction-account-ledger.component';
import { TransactionAccountLedgerDetailComponent } from './detail/transaction-account-ledger-detail.component';
import { TransactionAccountLedgerUpdateComponent } from './update/transaction-account-ledger-update.component';
import { TransactionAccountLedgerDeleteDialogComponent } from './delete/transaction-account-ledger-delete-dialog.component';
import { TransactionAccountLedgerRoutingModule } from './route/transaction-account-ledger-routing.module';

@NgModule({
  imports: [SharedModule, TransactionAccountLedgerRoutingModule],
  declarations: [
    TransactionAccountLedgerComponent,
    TransactionAccountLedgerDetailComponent,
    TransactionAccountLedgerUpdateComponent,
    TransactionAccountLedgerDeleteDialogComponent,
  ],
  entryComponents: [TransactionAccountLedgerDeleteDialogComponent],
})
export class TransactionAccountLedgerModule {}
