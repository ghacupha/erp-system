import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbDataSubmittingInstitutionsComponent } from './list/crb-data-submitting-institutions.component';
import { CrbDataSubmittingInstitutionsDetailComponent } from './detail/crb-data-submitting-institutions-detail.component';
import { CrbDataSubmittingInstitutionsUpdateComponent } from './update/crb-data-submitting-institutions-update.component';
import { CrbDataSubmittingInstitutionsDeleteDialogComponent } from './delete/crb-data-submitting-institutions-delete-dialog.component';
import { CrbDataSubmittingInstitutionsRoutingModule } from './route/crb-data-submitting-institutions-routing.module';

@NgModule({
  imports: [SharedModule, CrbDataSubmittingInstitutionsRoutingModule],
  declarations: [
    CrbDataSubmittingInstitutionsComponent,
    CrbDataSubmittingInstitutionsDetailComponent,
    CrbDataSubmittingInstitutionsUpdateComponent,
    CrbDataSubmittingInstitutionsDeleteDialogComponent,
  ],
  entryComponents: [CrbDataSubmittingInstitutionsDeleteDialogComponent],
})
export class CrbDataSubmittingInstitutionsModule {}
