import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChartOfAccountsCodeComponent } from '../list/chart-of-accounts-code.component';
import { ChartOfAccountsCodeDetailComponent } from '../detail/chart-of-accounts-code-detail.component';
import { ChartOfAccountsCodeUpdateComponent } from '../update/chart-of-accounts-code-update.component';
import { ChartOfAccountsCodeRoutingResolveService } from './chart-of-accounts-code-routing-resolve.service';

const chartOfAccountsCodeRoute: Routes = [
  {
    path: '',
    component: ChartOfAccountsCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChartOfAccountsCodeDetailComponent,
    resolve: {
      chartOfAccountsCode: ChartOfAccountsCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChartOfAccountsCodeUpdateComponent,
    resolve: {
      chartOfAccountsCode: ChartOfAccountsCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChartOfAccountsCodeUpdateComponent,
    resolve: {
      chartOfAccountsCode: ChartOfAccountsCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chartOfAccountsCodeRoute)],
  exports: [RouterModule],
})
export class ChartOfAccountsCodeRoutingModule {}
