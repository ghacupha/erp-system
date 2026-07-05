import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MonthlyPrepaymentOutstandingReportItemComponent } from './list/monthly-prepayment-outstanding-report-item.component';
import { MonthlyPrepaymentOutstandingReportItemDetailComponent } from './detail/monthly-prepayment-outstanding-report-item-detail.component';
import { MonthlyPrepaymentOutstandingReportItemRoutingModule } from './route/monthly-prepayment-outstanding-report-item-routing.module';

@NgModule({
  imports: [SharedModule, MonthlyPrepaymentOutstandingReportItemRoutingModule],
  declarations: [MonthlyPrepaymentOutstandingReportItemComponent, MonthlyPrepaymentOutstandingReportItemDetailComponent],
})
export class MonthlyPrepaymentOutstandingReportItemModule {}
