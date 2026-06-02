import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NbvCompilationJobComponent } from './list/nbv-compilation-job.component';
import { NbvCompilationJobDetailComponent } from './detail/nbv-compilation-job-detail.component';
import { NbvCompilationJobUpdateComponent } from './update/nbv-compilation-job-update.component';
import { NbvCompilationJobDeleteDialogComponent } from './delete/nbv-compilation-job-delete-dialog.component';
import { NbvCompilationJobRoutingModule } from './route/nbv-compilation-job-routing.module';

@NgModule({
  imports: [SharedModule, NbvCompilationJobRoutingModule],
  declarations: [
    NbvCompilationJobComponent,
    NbvCompilationJobDetailComponent,
    NbvCompilationJobUpdateComponent,
    NbvCompilationJobDeleteDialogComponent,
  ],
  entryComponents: [NbvCompilationJobDeleteDialogComponent],
})
export class NbvCompilationJobModule {}
