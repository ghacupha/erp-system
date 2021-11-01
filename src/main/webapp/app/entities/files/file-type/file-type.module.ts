import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FileTypeComponent } from './list/file-type.component';
import { FileTypeDetailComponent } from './detail/file-type-detail.component';
import { FileTypeUpdateComponent } from './update/file-type-update.component';
import { FileTypeDeleteDialogComponent } from './delete/file-type-delete-dialog.component';
import { FileTypeRoutingModule } from './route/file-type-routing.module';

@NgModule({
  imports: [SharedModule, FileTypeRoutingModule],
  declarations: [FileTypeComponent, FileTypeDetailComponent, FileTypeUpdateComponent, FileTypeDeleteDialogComponent],
  entryComponents: [FileTypeDeleteDialogComponent],
})
export class ErpServiceFileTypeModule {}
