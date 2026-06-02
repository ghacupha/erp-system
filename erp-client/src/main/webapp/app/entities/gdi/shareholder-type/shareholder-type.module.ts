import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShareholderTypeComponent } from './list/shareholder-type.component';
import { ShareholderTypeDetailComponent } from './detail/shareholder-type-detail.component';
import { ShareholderTypeUpdateComponent } from './update/shareholder-type-update.component';
import { ShareholderTypeDeleteDialogComponent } from './delete/shareholder-type-delete-dialog.component';
import { ShareholderTypeRoutingModule } from './route/shareholder-type-routing.module';

@NgModule({
  imports: [SharedModule, ShareholderTypeRoutingModule],
  declarations: [
    ShareholderTypeComponent,
    ShareholderTypeDetailComponent,
    ShareholderTypeUpdateComponent,
    ShareholderTypeDeleteDialogComponent,
  ],
  entryComponents: [ShareholderTypeDeleteDialogComponent],
})
export class ShareholderTypeModule {}
