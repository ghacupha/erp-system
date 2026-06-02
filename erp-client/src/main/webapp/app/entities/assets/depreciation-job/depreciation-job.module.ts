import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationJobComponent } from './list/depreciation-job.component';
import { DepreciationJobDetailComponent } from './detail/depreciation-job-detail.component';
import { DepreciationJobUpdateComponent } from './update/depreciation-job-update.component';
import { DepreciationJobDeleteDialogComponent } from './delete/depreciation-job-delete-dialog.component';
import { DepreciationJobRoutingModule } from './route/depreciation-job-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationJobRoutingModule],
  declarations: [
    DepreciationJobComponent,
    DepreciationJobDetailComponent,
    DepreciationJobUpdateComponent,
    DepreciationJobDeleteDialogComponent,
  ],
  entryComponents: [DepreciationJobDeleteDialogComponent],
})
export class DepreciationJobModule {}
