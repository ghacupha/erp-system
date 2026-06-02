import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityPostingReportItemComponent } from './list/lease-liability-posting-report-item.component';
import { LeaseLiabilityPostingReportItemDetailComponent } from './detail/lease-liability-posting-report-item-detail.component';
import { LeaseLiabilityPostingReportItemRoutingModule } from './route/lease-liability-posting-report-item-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityPostingReportItemRoutingModule],
  declarations: [LeaseLiabilityPostingReportItemComponent, LeaseLiabilityPostingReportItemDetailComponent],
})
export class LeaseLiabilityPostingReportItemModule {}
