import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbComplaintStatusTypeComponent } from './list/crb-complaint-status-type.component';
import { CrbComplaintStatusTypeDetailComponent } from './detail/crb-complaint-status-type-detail.component';
import { CrbComplaintStatusTypeUpdateComponent } from './update/crb-complaint-status-type-update.component';
import { CrbComplaintStatusTypeDeleteDialogComponent } from './delete/crb-complaint-status-type-delete-dialog.component';
import { CrbComplaintStatusTypeRoutingModule } from './route/crb-complaint-status-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbComplaintStatusTypeRoutingModule],
  declarations: [
    CrbComplaintStatusTypeComponent,
    CrbComplaintStatusTypeDetailComponent,
    CrbComplaintStatusTypeUpdateComponent,
    CrbComplaintStatusTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbComplaintStatusTypeDeleteDialogComponent],
})
export class CrbComplaintStatusTypeModule {}
