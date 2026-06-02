import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityScheduleReportItemComponent } from './list/lease-liability-schedule-report-item.component';
import { LeaseLiabilityScheduleReportItemDetailComponent } from './detail/lease-liability-schedule-report-item-detail.component';
import { LeaseLiabilityScheduleReportItemRoutingModule } from './route/lease-liability-schedule-report-item-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityScheduleReportItemRoutingModule],
  declarations: [LeaseLiabilityScheduleReportItemComponent, LeaseLiabilityScheduleReportItemDetailComponent],
})
export class LeaseLiabilityScheduleReportItemModule {}
