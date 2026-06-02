import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmortizationPostingReportComponent } from './list/amortization-posting-report.component';
import { AmortizationPostingReportDetailComponent } from './detail/amortization-posting-report-detail.component';
import { AmortizationPostingReportRoutingModule } from './route/amortization-posting-report-routing.module';

@NgModule({
  imports: [SharedModule, AmortizationPostingReportRoutingModule],
  declarations: [AmortizationPostingReportComponent, AmortizationPostingReportDetailComponent],
})
export class AmortizationPostingReportModule {}
