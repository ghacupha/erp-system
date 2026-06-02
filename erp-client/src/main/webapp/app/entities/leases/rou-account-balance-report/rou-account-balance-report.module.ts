import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RouAccountBalanceReportComponent } from './list/rou-account-balance-report.component';
import { RouAccountBalanceReportDetailComponent } from './detail/rou-account-balance-report-detail.component';
import { RouAccountBalanceReportUpdateComponent } from './update/rou-account-balance-report-update.component';
import { RouAccountBalanceReportDeleteDialogComponent } from './delete/rou-account-balance-report-delete-dialog.component';
import { RouAccountBalanceReportRoutingModule } from './route/rou-account-balance-report-routing.module';

@NgModule({
  imports: [SharedModule, RouAccountBalanceReportRoutingModule],
  declarations: [
    RouAccountBalanceReportComponent,
    RouAccountBalanceReportDetailComponent,
    RouAccountBalanceReportUpdateComponent,
    RouAccountBalanceReportDeleteDialogComponent,
  ],
  entryComponents: [RouAccountBalanceReportDeleteDialogComponent],
})
export class RouAccountBalanceReportModule {}
