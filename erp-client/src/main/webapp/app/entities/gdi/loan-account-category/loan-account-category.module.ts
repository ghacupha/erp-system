import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanAccountCategoryComponent } from './list/loan-account-category.component';
import { LoanAccountCategoryDetailComponent } from './detail/loan-account-category-detail.component';
import { LoanAccountCategoryUpdateComponent } from './update/loan-account-category-update.component';
import { LoanAccountCategoryDeleteDialogComponent } from './delete/loan-account-category-delete-dialog.component';
import { LoanAccountCategoryRoutingModule } from './route/loan-account-category-routing.module';

@NgModule({
  imports: [SharedModule, LoanAccountCategoryRoutingModule],
  declarations: [
    LoanAccountCategoryComponent,
    LoanAccountCategoryDetailComponent,
    LoanAccountCategoryUpdateComponent,
    LoanAccountCategoryDeleteDialogComponent,
  ],
  entryComponents: [LoanAccountCategoryDeleteDialogComponent],
})
export class LoanAccountCategoryModule {}
