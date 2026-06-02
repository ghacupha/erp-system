import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrepaymentReportComponent } from './list/prepayment-report.component';
import { PrepaymentReportDetailComponent } from './detail/prepayment-report-detail.component';
import { PrepaymentReportRoutingModule } from './route/prepayment-report-routing.module';

@NgModule({
  imports: [SharedModule, PrepaymentReportRoutingModule],
  declarations: [PrepaymentReportComponent, PrepaymentReportDetailComponent],
})
export class PrepaymentReportModule {}
