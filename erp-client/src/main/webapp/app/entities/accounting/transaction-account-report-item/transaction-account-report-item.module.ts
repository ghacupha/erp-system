import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TransactionAccountReportItemComponent } from './list/transaction-account-report-item.component';
import { TransactionAccountReportItemDetailComponent } from './detail/transaction-account-report-item-detail.component';
import { TransactionAccountReportItemRoutingModule } from './route/transaction-account-report-item-routing.module';

@NgModule({
  imports: [SharedModule, TransactionAccountReportItemRoutingModule],
  declarations: [TransactionAccountReportItemComponent, TransactionAccountReportItemDetailComponent],
})
export class TransactionAccountReportItemModule {}
