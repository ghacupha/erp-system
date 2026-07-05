import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbFileTransmissionStatusComponent } from './list/crb-file-transmission-status.component';
import { CrbFileTransmissionStatusDetailComponent } from './detail/crb-file-transmission-status-detail.component';
import { CrbFileTransmissionStatusUpdateComponent } from './update/crb-file-transmission-status-update.component';
import { CrbFileTransmissionStatusDeleteDialogComponent } from './delete/crb-file-transmission-status-delete-dialog.component';
import { CrbFileTransmissionStatusRoutingModule } from './route/crb-file-transmission-status-routing.module';

@NgModule({
  imports: [SharedModule, CrbFileTransmissionStatusRoutingModule],
  declarations: [
    CrbFileTransmissionStatusComponent,
    CrbFileTransmissionStatusDetailComponent,
    CrbFileTransmissionStatusUpdateComponent,
    CrbFileTransmissionStatusDeleteDialogComponent,
  ],
  entryComponents: [CrbFileTransmissionStatusDeleteDialogComponent],
})
export class CrbFileTransmissionStatusModule {}
