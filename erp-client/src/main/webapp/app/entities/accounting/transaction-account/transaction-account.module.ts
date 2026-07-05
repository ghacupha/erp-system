import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionAccountComponent } from './list/transaction-account.component';
import { TransactionAccountDetailComponent } from './detail/transaction-account-detail.component';
import { TransactionAccountUpdateComponent } from './update/transaction-account-update.component';
import { TransactionAccountDeleteDialogComponent } from './delete/transaction-account-delete-dialog.component';
import { TransactionAccountRoutingModule } from './route/transaction-account-routing.module';

@NgModule({
  imports: [SharedModule, TransactionAccountRoutingModule],
  declarations: [
    TransactionAccountComponent,
    TransactionAccountDetailComponent,
    TransactionAccountUpdateComponent,
    TransactionAccountDeleteDialogComponent,
  ],
  entryComponents: [TransactionAccountDeleteDialogComponent],
})
export class TransactionAccountModule {}
