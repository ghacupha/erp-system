import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbCreditApplicationStatusComponent } from '../list/crb-credit-application-status.component';
import { CrbCreditApplicationStatusDetailComponent } from '../detail/crb-credit-application-status-detail.component';
import { CrbCreditApplicationStatusUpdateComponent } from '../update/crb-credit-application-status-update.component';
import { CrbCreditApplicationStatusRoutingResolveService } from './crb-credit-application-status-routing-resolve.service';

const crbCreditApplicationStatusRoute: Routes = [
  {
    path: '',
    component: CrbCreditApplicationStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbCreditApplicationStatusDetailComponent,
    resolve: {
      crbCreditApplicationStatus: CrbCreditApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbCreditApplicationStatusUpdateComponent,
    resolve: {
      crbCreditApplicationStatus: CrbCreditApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbCreditApplicationStatusUpdateComponent,
    resolve: {
      crbCreditApplicationStatus: CrbCreditApplicationStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbCreditApplicationStatusRoute)],
  exports: [RouterModule],
})
export class CrbCreditApplicationStatusRoutingModule {}
