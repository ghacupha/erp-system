import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProcessStatusComponent } from './list/process-status.component';
import { ProcessStatusDetailComponent } from './detail/process-status-detail.component';
import { ProcessStatusUpdateComponent } from './update/process-status-update.component';
import { ProcessStatusDeleteDialogComponent } from './delete/process-status-delete-dialog.component';
import { ProcessStatusRoutingModule } from './route/process-status-routing.module';

@NgModule({
  imports: [SharedModule, ProcessStatusRoutingModule],
  declarations: [ProcessStatusComponent, ProcessStatusDetailComponent, ProcessStatusUpdateComponent, ProcessStatusDeleteDialogComponent],
  entryComponents: [ProcessStatusDeleteDialogComponent],
})
export class ProcessStatusModule {}
