import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationEntryComponent } from './list/depreciation-entry.component';
import { DepreciationEntryDetailComponent } from './detail/depreciation-entry-detail.component';
import { DepreciationEntryUpdateComponent } from './update/depreciation-entry-update.component';
import { DepreciationEntryDeleteDialogComponent } from './delete/depreciation-entry-delete-dialog.component';
import { DepreciationEntryRoutingModule } from './route/depreciation-entry-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationEntryRoutingModule],
  declarations: [
    DepreciationEntryComponent,
    DepreciationEntryDetailComponent,
    DepreciationEntryUpdateComponent,
    DepreciationEntryDeleteDialogComponent,
  ],
  entryComponents: [DepreciationEntryDeleteDialogComponent],
})
export class DepreciationEntryModule {}
