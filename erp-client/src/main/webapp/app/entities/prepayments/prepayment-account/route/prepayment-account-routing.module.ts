import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentAccountComponent } from '../list/prepayment-account.component';
import { PrepaymentAccountDetailComponent } from '../detail/prepayment-account-detail.component';
import { PrepaymentAccountUpdateComponent } from '../update/prepayment-account-update.component';
import { PrepaymentAccountRoutingResolveService } from './prepayment-account-routing-resolve.service';

const prepaymentAccountRoute: Routes = [
  {
    path: '',
    component: PrepaymentAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentAccountDetailComponent,
    resolve: {
      prepaymentAccount: PrepaymentAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentAccountUpdateComponent,
    resolve: {
      prepaymentAccount: PrepaymentAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentAccountUpdateComponent,
    resolve: {
      prepaymentAccount: PrepaymentAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentAccountRoute)],
  exports: [RouterModule],
})
export class PrepaymentAccountRoutingModule {}
