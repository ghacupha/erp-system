import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentAccountReportComponent } from './list/prepayment-account-report.component';
import { PrepaymentAccountReportDetailComponent } from './detail/prepayment-account-report-detail.component';
import { PrepaymentAccountReportRoutingModule } from './route/prepayment-account-report-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentAccountReportRoutingModule],
  declarations: [PrepaymentAccountReportComponent, PrepaymentAccountReportDetailComponent],
})
export class PrepaymentAccountReportModule {}
