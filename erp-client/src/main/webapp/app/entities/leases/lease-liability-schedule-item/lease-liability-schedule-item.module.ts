import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityScheduleItemComponent } from './list/lease-liability-schedule-item.component';
import { LeaseLiabilityScheduleItemDetailComponent } from './detail/lease-liability-schedule-item-detail.component';
import { LeaseLiabilityScheduleItemUpdateComponent } from './update/lease-liability-schedule-item-update.component';
import { LeaseLiabilityScheduleItemDeleteDialogComponent } from './delete/lease-liability-schedule-item-delete-dialog.component';
import { LeaseLiabilityScheduleItemRoutingModule } from './route/lease-liability-schedule-item-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityScheduleItemRoutingModule],
  declarations: [
    LeaseLiabilityScheduleItemComponent,
    LeaseLiabilityScheduleItemDetailComponent,
    LeaseLiabilityScheduleItemUpdateComponent,
    LeaseLiabilityScheduleItemDeleteDialogComponent,
  ],
  entryComponents: [LeaseLiabilityScheduleItemDeleteDialogComponent],
})
export class LeaseLiabilityScheduleItemModule {}
