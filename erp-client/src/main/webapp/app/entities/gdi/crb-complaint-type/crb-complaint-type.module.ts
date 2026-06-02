import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbComplaintTypeComponent } from './list/crb-complaint-type.component';
import { CrbComplaintTypeDetailComponent } from './detail/crb-complaint-type-detail.component';
import { CrbComplaintTypeUpdateComponent } from './update/crb-complaint-type-update.component';
import { CrbComplaintTypeDeleteDialogComponent } from './delete/crb-complaint-type-delete-dialog.component';
import { CrbComplaintTypeRoutingModule } from './route/crb-complaint-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbComplaintTypeRoutingModule],
  declarations: [
    CrbComplaintTypeComponent,
    CrbComplaintTypeDetailComponent,
    CrbComplaintTypeUpdateComponent,
    CrbComplaintTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbComplaintTypeDeleteDialogComponent],
})
export class CrbComplaintTypeModule {}
