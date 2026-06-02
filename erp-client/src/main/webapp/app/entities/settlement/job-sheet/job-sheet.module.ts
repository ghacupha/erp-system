import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { JobSheetComponent } from './list/job-sheet.component';
import { JobSheetDetailComponent } from './detail/job-sheet-detail.component';
import { JobSheetUpdateComponent } from './update/job-sheet-update.component';
import { JobSheetDeleteDialogComponent } from './delete/job-sheet-delete-dialog.component';
import { JobSheetRoutingModule } from './route/job-sheet-routing.module';

@NgModule({
  imports: [SharedModule, JobSheetRoutingModule],
  declarations: [JobSheetComponent, JobSheetDetailComponent, JobSheetUpdateComponent, JobSheetDeleteDialogComponent],
  entryComponents: [JobSheetDeleteDialogComponent],
})
export class JobSheetModule {}
