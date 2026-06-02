import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanRestructureFlagComponent } from './list/loan-restructure-flag.component';
import { LoanRestructureFlagDetailComponent } from './detail/loan-restructure-flag-detail.component';
import { LoanRestructureFlagUpdateComponent } from './update/loan-restructure-flag-update.component';
import { LoanRestructureFlagDeleteDialogComponent } from './delete/loan-restructure-flag-delete-dialog.component';
import { LoanRestructureFlagRoutingModule } from './route/loan-restructure-flag-routing.module';

@NgModule({
  imports: [SharedModule, LoanRestructureFlagRoutingModule],
  declarations: [
    LoanRestructureFlagComponent,
    LoanRestructureFlagDetailComponent,
    LoanRestructureFlagUpdateComponent,
    LoanRestructureFlagDeleteDialogComponent,
  ],
  entryComponents: [LoanRestructureFlagDeleteDialogComponent],
})
export class LoanRestructureFlagModule {}
