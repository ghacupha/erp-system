import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbSubscriptionStatusTypeCodeComponent } from '../list/crb-subscription-status-type-code.component';
import { CrbSubscriptionStatusTypeCodeDetailComponent } from '../detail/crb-subscription-status-type-code-detail.component';
import { CrbSubscriptionStatusTypeCodeUpdateComponent } from '../update/crb-subscription-status-type-code-update.component';
import { CrbSubscriptionStatusTypeCodeRoutingResolveService } from './crb-subscription-status-type-code-routing-resolve.service';

const crbSubscriptionStatusTypeCodeRoute: Routes = [
  {
    path: '',
    component: CrbSubscriptionStatusTypeCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbSubscriptionStatusTypeCodeDetailComponent,
    resolve: {
      crbSubscriptionStatusTypeCode: CrbSubscriptionStatusTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbSubscriptionStatusTypeCodeUpdateComponent,
    resolve: {
      crbSubscriptionStatusTypeCode: CrbSubscriptionStatusTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbSubscriptionStatusTypeCodeUpdateComponent,
    resolve: {
      crbSubscriptionStatusTypeCode: CrbSubscriptionStatusTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbSubscriptionStatusTypeCodeRoute)],
  exports: [RouterModule],
})
export class CrbSubscriptionStatusTypeCodeRoutingModule {}
