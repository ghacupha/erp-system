import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemContentTypeComponent } from './list/system-content-type.component';
import { SystemContentTypeDetailComponent } from './detail/system-content-type-detail.component';
import { SystemContentTypeUpdateComponent } from './update/system-content-type-update.component';
import { SystemContentTypeDeleteDialogComponent } from './delete/system-content-type-delete-dialog.component';
import { SystemContentTypeRoutingModule } from './route/system-content-type-routing.module';

@NgModule({
  imports: [SharedModule, SystemContentTypeRoutingModule],
  declarations: [
    SystemContentTypeComponent,
    SystemContentTypeDetailComponent,
    SystemContentTypeUpdateComponent,
    SystemContentTypeDeleteDialogComponent,
  ],
  entryComponents: [SystemContentTypeDeleteDialogComponent],
})
export class SystemContentTypeModule {}
