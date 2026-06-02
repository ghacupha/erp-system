import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbComplaintStatusTypeComponent } from '../list/crb-complaint-status-type.component';
import { CrbComplaintStatusTypeDetailComponent } from '../detail/crb-complaint-status-type-detail.component';
import { CrbComplaintStatusTypeUpdateComponent } from '../update/crb-complaint-status-type-update.component';
import { CrbComplaintStatusTypeRoutingResolveService } from './crb-complaint-status-type-routing-resolve.service';

const crbComplaintStatusTypeRoute: Routes = [
  {
    path: '',
    component: CrbComplaintStatusTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbComplaintStatusTypeDetailComponent,
    resolve: {
      crbComplaintStatusType: CrbComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbComplaintStatusTypeUpdateComponent,
    resolve: {
      crbComplaintStatusType: CrbComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbComplaintStatusTypeUpdateComponent,
    resolve: {
      crbComplaintStatusType: CrbComplaintStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbComplaintStatusTypeRoute)],
  exports: [RouterModule],
})
export class CrbComplaintStatusTypeRoutingModule {}
