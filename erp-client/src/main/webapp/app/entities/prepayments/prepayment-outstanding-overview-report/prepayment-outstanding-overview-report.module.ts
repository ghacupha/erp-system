import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentOutstandingOverviewReportComponent } from './list/prepayment-outstanding-overview-report.component';
import { PrepaymentOutstandingOverviewReportDetailComponent } from './detail/prepayment-outstanding-overview-report-detail.component';
import { PrepaymentOutstandingOverviewReportRoutingModule } from './route/prepayment-outstanding-overview-report-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentOutstandingOverviewReportRoutingModule],
  declarations: [PrepaymentOutstandingOverviewReportComponent, PrepaymentOutstandingOverviewReportDetailComponent],
})
export class PrepaymentOutstandingOverviewReportModule {}
