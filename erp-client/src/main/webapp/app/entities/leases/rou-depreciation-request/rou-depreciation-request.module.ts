import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouDepreciationRequestComponent } from './list/rou-depreciation-request.component';
import { RouDepreciationRequestDetailComponent } from './detail/rou-depreciation-request-detail.component';
import { RouDepreciationRequestUpdateComponent } from './update/rou-depreciation-request-update.component';
import { RouDepreciationRequestDeleteDialogComponent } from './delete/rou-depreciation-request-delete-dialog.component';
import { RouDepreciationRequestRoutingModule } from './route/rou-depreciation-request-routing.module';

@NgModule({
  imports: [SharedModule, RouDepreciationRequestRoutingModule],
  declarations: [
    RouDepreciationRequestComponent,
    RouDepreciationRequestDetailComponent,
    RouDepreciationRequestUpdateComponent,
    RouDepreciationRequestDeleteDialogComponent,
  ],
  entryComponents: [RouDepreciationRequestDeleteDialogComponent],
})
export class RouDepreciationRequestModule {}
