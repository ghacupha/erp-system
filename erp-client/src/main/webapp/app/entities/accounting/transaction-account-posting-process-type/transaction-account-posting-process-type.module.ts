import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionAccountPostingProcessTypeComponent } from './list/transaction-account-posting-process-type.component';
import { TransactionAccountPostingProcessTypeDetailComponent } from './detail/transaction-account-posting-process-type-detail.component';
import { TransactionAccountPostingProcessTypeUpdateComponent } from './update/transaction-account-posting-process-type-update.component';
import { TransactionAccountPostingProcessTypeDeleteDialogComponent } from './delete/transaction-account-posting-process-type-delete-dialog.component';
import { TransactionAccountPostingProcessTypeRoutingModule } from './route/transaction-account-posting-process-type-routing.module';

@NgModule({
  imports: [SharedModule, TransactionAccountPostingProcessTypeRoutingModule],
  declarations: [
    TransactionAccountPostingProcessTypeComponent,
    TransactionAccountPostingProcessTypeDetailComponent,
    TransactionAccountPostingProcessTypeUpdateComponent,
    TransactionAccountPostingProcessTypeDeleteDialogComponent,
  ],
  entryComponents: [TransactionAccountPostingProcessTypeDeleteDialogComponent],
})
export class TransactionAccountPostingProcessTypeModule {}
