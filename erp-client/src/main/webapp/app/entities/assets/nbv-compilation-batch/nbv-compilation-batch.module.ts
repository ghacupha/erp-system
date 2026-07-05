import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NbvCompilationBatchComponent } from './list/nbv-compilation-batch.component';
import { NbvCompilationBatchDetailComponent } from './detail/nbv-compilation-batch-detail.component';
import { NbvCompilationBatchUpdateComponent } from './update/nbv-compilation-batch-update.component';
import { NbvCompilationBatchDeleteDialogComponent } from './delete/nbv-compilation-batch-delete-dialog.component';
import { NbvCompilationBatchRoutingModule } from './route/nbv-compilation-batch-routing.module';

@NgModule({
  imports: [SharedModule, NbvCompilationBatchRoutingModule],
  declarations: [
    NbvCompilationBatchComponent,
    NbvCompilationBatchDetailComponent,
    NbvCompilationBatchUpdateComponent,
    NbvCompilationBatchDeleteDialogComponent,
  ],
  entryComponents: [NbvCompilationBatchDeleteDialogComponent],
})
export class NbvCompilationBatchModule {}
