import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ErpGatewaySharedModule } from 'app/shared/shared.module';
import { FileUploadComponent } from './file-upload.component';
import { FileUploadDetailComponent } from './file-upload-detail.component';
import { FileUploadUpdateComponent } from './file-upload-update.component';
import { FileUploadDeleteDialogComponent } from './file-upload-delete-dialog.component';
import { fileUploadRoute } from './file-upload.route';

@NgModule({
  imports: [ErpGatewaySharedModule, RouterModule.forChild(fileUploadRoute)],
  declarations: [FileUploadComponent, FileUploadDetailComponent, FileUploadUpdateComponent, FileUploadDeleteDialogComponent],
  entryComponents: [FileUploadDeleteDialogComponent],
})
export class ErpServiceFileUploadModule {}
