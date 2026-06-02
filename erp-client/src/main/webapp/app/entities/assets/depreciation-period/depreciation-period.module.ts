import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepreciationPeriodComponent } from './list/depreciation-period.component';
import { DepreciationPeriodDetailComponent } from './detail/depreciation-period-detail.component';
import { DepreciationPeriodUpdateComponent } from './update/depreciation-period-update.component';
import { DepreciationPeriodDeleteDialogComponent } from './delete/depreciation-period-delete-dialog.component';
import { DepreciationPeriodRoutingModule } from './route/depreciation-period-routing.module';

@NgModule({
  imports: [SharedModule, DepreciationPeriodRoutingModule],
  declarations: [
    DepreciationPeriodComponent,
    DepreciationPeriodDetailComponent,
    DepreciationPeriodUpdateComponent,
    DepreciationPeriodDeleteDialogComponent,
  ],
  entryComponents: [DepreciationPeriodDeleteDialogComponent],
})
export class DepreciationPeriodModule {}
