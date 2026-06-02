import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbAccountStatusComponent } from '../list/crb-account-status.component';
import { CrbAccountStatusDetailComponent } from '../detail/crb-account-status-detail.component';
import { CrbAccountStatusUpdateComponent } from '../update/crb-account-status-update.component';
import { CrbAccountStatusRoutingResolveService } from './crb-account-status-routing-resolve.service';

const crbAccountStatusRoute: Routes = [
  {
    path: '',
    component: CrbAccountStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbAccountStatusDetailComponent,
    resolve: {
      crbAccountStatus: CrbAccountStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbAccountStatusUpdateComponent,
    resolve: {
      crbAccountStatus: CrbAccountStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbAccountStatusUpdateComponent,
    resolve: {
      crbAccountStatus: CrbAccountStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbAccountStatusRoute)],
  exports: [RouterModule],
})
export class CrbAccountStatusRoutingModule {}
