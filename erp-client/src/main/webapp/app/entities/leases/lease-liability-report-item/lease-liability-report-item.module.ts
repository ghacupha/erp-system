import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityReportItemComponent } from './list/lease-liability-report-item.component';
import { LeaseLiabilityReportItemDetailComponent } from './detail/lease-liability-report-item-detail.component';
import { LeaseLiabilityReportItemRoutingModule } from './route/lease-liability-report-item-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityReportItemRoutingModule],
  declarations: [LeaseLiabilityReportItemComponent, LeaseLiabilityReportItemDetailComponent],
})
export class LeaseLiabilityReportItemModule {}
