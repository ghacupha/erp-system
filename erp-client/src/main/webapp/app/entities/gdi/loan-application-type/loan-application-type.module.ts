import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LoanApplicationTypeComponent } from './list/loan-application-type.component';
import { LoanApplicationTypeDetailComponent } from './detail/loan-application-type-detail.component';
import { LoanApplicationTypeUpdateComponent } from './update/loan-application-type-update.component';
import { LoanApplicationTypeDeleteDialogComponent } from './delete/loan-application-type-delete-dialog.component';
import { LoanApplicationTypeRoutingModule } from './route/loan-application-type-routing.module';

@NgModule({
  imports: [SharedModule, LoanApplicationTypeRoutingModule],
  declarations: [
    LoanApplicationTypeComponent,
    LoanApplicationTypeDetailComponent,
    LoanApplicationTypeUpdateComponent,
    LoanApplicationTypeDeleteDialogComponent,
  ],
  entryComponents: [LoanApplicationTypeDeleteDialogComponent],
})
export class LoanApplicationTypeModule {}
