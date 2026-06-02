import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationJobComponent } from '../list/depreciation-job.component';
import { DepreciationJobDetailComponent } from '../detail/depreciation-job-detail.component';
import { DepreciationJobUpdateComponent } from '../update/depreciation-job-update.component';
import { DepreciationJobRoutingResolveService } from './depreciation-job-routing-resolve.service';

const depreciationJobRoute: Routes = [
  {
    path: '',
    component: DepreciationJobComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationJobDetailComponent,
    resolve: {
      depreciationJob: DepreciationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationJobUpdateComponent,
    resolve: {
      depreciationJob: DepreciationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationJobUpdateComponent,
    resolve: {
      depreciationJob: DepreciationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationJobRoute)],
  exports: [RouterModule],
})
export class DepreciationJobRoutingModule {}
