import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationJobNoticeComponent } from '../list/depreciation-job-notice.component';
import { DepreciationJobNoticeDetailComponent } from '../detail/depreciation-job-notice-detail.component';
import { DepreciationJobNoticeUpdateComponent } from '../update/depreciation-job-notice-update.component';
import { DepreciationJobNoticeRoutingResolveService } from './depreciation-job-notice-routing-resolve.service';

const depreciationJobNoticeRoute: Routes = [
  {
    path: '',
    component: DepreciationJobNoticeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationJobNoticeDetailComponent,
    resolve: {
      depreciationJobNotice: DepreciationJobNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationJobNoticeUpdateComponent,
    resolve: {
      depreciationJobNotice: DepreciationJobNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationJobNoticeUpdateComponent,
    resolve: {
      depreciationJobNotice: DepreciationJobNoticeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationJobNoticeRoute)],
  exports: [RouterModule],
})
export class DepreciationJobNoticeRoutingModule {}
