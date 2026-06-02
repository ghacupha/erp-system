import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeasePeriodComponent } from './list/lease-period.component';
import { LeasePeriodDetailComponent } from './detail/lease-period-detail.component';
import { LeasePeriodUpdateComponent } from './update/lease-period-update.component';
import { LeasePeriodDeleteDialogComponent } from './delete/lease-period-delete-dialog.component';
import { LeasePeriodRoutingModule } from './route/lease-period-routing.module';

@NgModule({
  imports: [SharedModule, LeasePeriodRoutingModule],
  declarations: [LeasePeriodComponent, LeasePeriodDetailComponent, LeasePeriodUpdateComponent, LeasePeriodDeleteDialogComponent],
  entryComponents: [LeasePeriodDeleteDialogComponent],
})
export class LeasePeriodModule {}
