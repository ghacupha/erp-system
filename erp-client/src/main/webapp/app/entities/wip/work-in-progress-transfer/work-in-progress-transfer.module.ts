import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkInProgressTransferComponent } from './list/work-in-progress-transfer.component';
import { WorkInProgressTransferDetailComponent } from './detail/work-in-progress-transfer-detail.component';
import { WorkInProgressTransferUpdateComponent } from './update/work-in-progress-transfer-update.component';
import { WorkInProgressTransferDeleteDialogComponent } from './delete/work-in-progress-transfer-delete-dialog.component';
import { WorkInProgressTransferRoutingModule } from './route/work-in-progress-transfer-routing.module';

@NgModule({
  imports: [SharedModule, WorkInProgressTransferRoutingModule],
  declarations: [
    WorkInProgressTransferComponent,
    WorkInProgressTransferDetailComponent,
    WorkInProgressTransferUpdateComponent,
    WorkInProgressTransferDeleteDialogComponent,
  ],
  entryComponents: [WorkInProgressTransferDeleteDialogComponent],
})
export class WorkInProgressTransferModule {}
