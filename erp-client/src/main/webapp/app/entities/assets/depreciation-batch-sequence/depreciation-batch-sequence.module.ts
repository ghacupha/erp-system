import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationBatchSequenceComponent } from './list/depreciation-batch-sequence.component';
import { DepreciationBatchSequenceDetailComponent } from './detail/depreciation-batch-sequence-detail.component';
import { DepreciationBatchSequenceUpdateComponent } from './update/depreciation-batch-sequence-update.component';
import { DepreciationBatchSequenceDeleteDialogComponent } from './delete/depreciation-batch-sequence-delete-dialog.component';
import { DepreciationBatchSequenceRoutingModule } from './route/depreciation-batch-sequence-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationBatchSequenceRoutingModule],
  declarations: [
    DepreciationBatchSequenceComponent,
    DepreciationBatchSequenceDetailComponent,
    DepreciationBatchSequenceUpdateComponent,
    DepreciationBatchSequenceDeleteDialogComponent,
  ],
  entryComponents: [DepreciationBatchSequenceDeleteDialogComponent],
})
export class DepreciationBatchSequenceModule {}
