import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankBranchCodeComponent } from './list/bank-branch-code.component';
import { BankBranchCodeDetailComponent } from './detail/bank-branch-code-detail.component';
import { BankBranchCodeUpdateComponent } from './update/bank-branch-code-update.component';
import { BankBranchCodeDeleteDialogComponent } from './delete/bank-branch-code-delete-dialog.component';
import { BankBranchCodeRoutingModule } from './route/bank-branch-code-routing.module';

@NgModule({
  imports: [SharedModule, BankBranchCodeRoutingModule],
  declarations: [
    BankBranchCodeComponent,
    BankBranchCodeDetailComponent,
    BankBranchCodeUpdateComponent,
    BankBranchCodeDeleteDialogComponent,
  ],
  entryComponents: [BankBranchCodeDeleteDialogComponent],
})
export class BankBranchCodeModule {}
