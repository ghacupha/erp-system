import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionAccountCategoryComponent } from './list/transaction-account-category.component';
import { TransactionAccountCategoryDetailComponent } from './detail/transaction-account-category-detail.component';
import { TransactionAccountCategoryUpdateComponent } from './update/transaction-account-category-update.component';
import { TransactionAccountCategoryDeleteDialogComponent } from './delete/transaction-account-category-delete-dialog.component';
import { TransactionAccountCategoryRoutingModule } from './route/transaction-account-category-routing.module';

@NgModule({
  imports: [SharedModule, TransactionAccountCategoryRoutingModule],
  declarations: [
    TransactionAccountCategoryComponent,
    TransactionAccountCategoryDetailComponent,
    TransactionAccountCategoryUpdateComponent,
    TransactionAccountCategoryDeleteDialogComponent,
  ],
  entryComponents: [TransactionAccountCategoryDeleteDialogComponent],
})
export class TransactionAccountCategoryModule {}
