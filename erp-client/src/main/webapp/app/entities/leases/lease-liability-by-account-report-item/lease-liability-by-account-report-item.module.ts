import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaseLiabilityByAccountReportItemComponent } from './list/lease-liability-by-account-report-item.component';
import { LeaseLiabilityByAccountReportItemDetailComponent } from './detail/lease-liability-by-account-report-item-detail.component';
import { LeaseLiabilityByAccountReportItemRoutingModule } from './route/lease-liability-by-account-report-item-routing.module';

@NgModule({
  imports: [SharedModule, LeaseLiabilityByAccountReportItemRoutingModule],
  declarations: [LeaseLiabilityByAccountReportItemComponent, LeaseLiabilityByAccountReportItemDetailComponent],
})
export class LeaseLiabilityByAccountReportItemModule {}
