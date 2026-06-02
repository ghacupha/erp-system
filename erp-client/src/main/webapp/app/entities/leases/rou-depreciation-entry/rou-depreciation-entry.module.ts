import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationEntryComponent } from './list/rou-depreciation-entry.component';
import { RouDepreciationEntryDetailComponent } from './detail/rou-depreciation-entry-detail.component';
import { RouDepreciationEntryUpdateComponent } from './update/rou-depreciation-entry-update.component';
import { RouDepreciationEntryDeleteDialogComponent } from './delete/rou-depreciation-entry-delete-dialog.component';
import { RouDepreciationEntryRoutingModule } from './route/rou-depreciation-entry-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationEntryRoutingModule],
  declarations: [
    RouDepreciationEntryComponent,
    RouDepreciationEntryDetailComponent,
    RouDepreciationEntryUpdateComponent,
    RouDepreciationEntryDeleteDialogComponent,
  ],
  entryComponents: [RouDepreciationEntryDeleteDialogComponent],
})
export class RouDepreciationEntryModule {}
