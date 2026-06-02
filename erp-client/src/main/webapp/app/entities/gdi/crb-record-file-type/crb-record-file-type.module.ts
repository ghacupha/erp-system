import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbRecordFileTypeComponent } from './list/crb-record-file-type.component';
import { CrbRecordFileTypeDetailComponent } from './detail/crb-record-file-type-detail.component';
import { CrbRecordFileTypeUpdateComponent } from './update/crb-record-file-type-update.component';
import { CrbRecordFileTypeDeleteDialogComponent } from './delete/crb-record-file-type-delete-dialog.component';
import { CrbRecordFileTypeRoutingModule } from './route/crb-record-file-type-routing.module';

@NgModule({
  imports: [SharedModule, CrbRecordFileTypeRoutingModule],
  declarations: [
    CrbRecordFileTypeComponent,
    CrbRecordFileTypeDetailComponent,
    CrbRecordFileTypeUpdateComponent,
    CrbRecordFileTypeDeleteDialogComponent,
  ],
  entryComponents: [CrbRecordFileTypeDeleteDialogComponent],
})
export class CrbRecordFileTypeModule {}
