import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransactionAccountReportItemComponent } from '../list/transaction-account-report-item.component';
import { TransactionAccountReportItemDetailComponent } from '../detail/transaction-account-report-item-detail.component';
import { TransactionAccountReportItemRoutingResolveService } from './transaction-account-report-item-routing-resolve.service';

const transactionAccountReportItemRoute: Routes = [
  {
    path: '',
    component: TransactionAccountReportItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountReportItemDetailComponent,
    resolve: {
      transactionAccountReportItem: TransactionAccountReportItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountReportItemRoute)],
  exports: [RouterModule],
})
export class TransactionAccountReportItemRoutingModule {}
