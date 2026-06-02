import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgencyNoticeComponent } from '../list/agency-notice.component';
import { AgencyNoticeDetailComponent } from '../detail/agency-notice-detail.component';
import { AgencyNoticeUpdateComponent } from '../update/agency-notice-update.component';
import { AgencyNoticeRoutingResolveService } from './agency-notice-routing-resolve.service';

const agencyNoticeRoute: Routes = [
  {
    path: '',
    component: AgencyNoticeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgencyNoticeDetailComponent,
    resolve: {
      agencyNotice: AgencyNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgencyNoticeUpdateComponent,
    resolve: {
      agencyNotice: AgencyNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgencyNoticeUpdateComponent,
    resolve: {
      agencyNotice: AgencyNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(agencyNoticeRoute)],
  exports: [RouterModule],
})
export class AgencyNoticeRoutingModule {}
