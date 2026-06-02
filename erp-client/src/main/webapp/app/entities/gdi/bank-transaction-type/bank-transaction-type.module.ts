import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankTransactionTypeComponent } from './list/bank-transaction-type.component';
import { BankTransactionTypeDetailComponent } from './detail/bank-transaction-type-detail.component';
import { BankTransactionTypeUpdateComponent } from './update/bank-transaction-type-update.component';
import { BankTransactionTypeDeleteDialogComponent } from './delete/bank-transaction-type-delete-dialog.component';
import { BankTransactionTypeRoutingModule } from './route/bank-transaction-type-routing.module';

@NgModule({
  imports: [SharedModule, BankTransactionTypeRoutingModule],
  declarations: [
    BankTransactionTypeComponent,
    BankTransactionTypeDetailComponent,
    BankTransactionTypeUpdateComponent,
    BankTransactionTypeDeleteDialogComponent,
  ],
  entryComponents: [BankTransactionTypeDeleteDialogComponent],
})
export class BankTransactionTypeModule {}
