import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAccountBalanceReportItemComponent } from './list/rou-account-balance-report-item.component';
import { RouAccountBalanceReportItemDetailComponent } from './detail/rou-account-balance-report-item-detail.component';
import { RouAccountBalanceReportItemRoutingModule } from './route/rou-account-balance-report-item-routing.module';

@NgModule({
  imports: [SharedModule, RouAccountBalanceReportItemRoutingModule],
  declarations: [RouAccountBalanceReportItemComponent, RouAccountBalanceReportItemDetailComponent],
})
export class RouAccountBalanceReportItemModule {}
