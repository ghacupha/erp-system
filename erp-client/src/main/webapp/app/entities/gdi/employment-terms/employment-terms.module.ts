import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmploymentTermsComponent } from './list/employment-terms.component';
import { EmploymentTermsDetailComponent } from './detail/employment-terms-detail.component';
import { EmploymentTermsUpdateComponent } from './update/employment-terms-update.component';
import { EmploymentTermsDeleteDialogComponent } from './delete/employment-terms-delete-dialog.component';
import { EmploymentTermsRoutingModule } from './route/employment-terms-routing.module';

@NgModule({
  imports: [SharedModule, EmploymentTermsRoutingModule],
  declarations: [
    EmploymentTermsComponent,
    EmploymentTermsDetailComponent,
    EmploymentTermsUpdateComponent,
    EmploymentTermsDeleteDialogComponent,
  ],
  entryComponents: [EmploymentTermsDeleteDialogComponent],
})
export class EmploymentTermsModule {}
