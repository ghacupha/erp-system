import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileUploadComponent } from './list/file-upload.component';
import { FileUploadDetailComponent } from './detail/file-upload-detail.component';
import { FileUploadUpdateComponent } from './update/file-upload-update.component';
import { FileUploadDeleteDialogComponent } from './delete/file-upload-delete-dialog.component';
import { FileUploadRoutingModule } from './route/file-upload-routing.module';

@NgModule({
  imports: [SharedModule, FileUploadRoutingModule],
  declarations: [FileUploadComponent, FileUploadDetailComponent, FileUploadUpdateComponent, FileUploadDeleteDialogComponent],
  entryComponents: [FileUploadDeleteDialogComponent],
})
export class ErpServiceFileUploadModule {}
