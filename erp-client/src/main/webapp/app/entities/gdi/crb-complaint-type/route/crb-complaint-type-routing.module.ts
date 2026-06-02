import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbComplaintTypeComponent } from '../list/crb-complaint-type.component';
import { CrbComplaintTypeDetailComponent } from '../detail/crb-complaint-type-detail.component';
import { CrbComplaintTypeUpdateComponent } from '../update/crb-complaint-type-update.component';
import { CrbComplaintTypeRoutingResolveService } from './crb-complaint-type-routing-resolve.service';

const crbComplaintTypeRoute: Routes = [
  {
    path: '',
    component: CrbComplaintTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbComplaintTypeDetailComponent,
    resolve: {
      crbComplaintType: CrbComplaintTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbComplaintTypeUpdateComponent,
    resolve: {
      crbComplaintType: CrbComplaintTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbComplaintTypeUpdateComponent,
    resolve: {
      crbComplaintType: CrbComplaintTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbComplaintTypeRoute)],
  exports: [RouterModule],
})
export class CrbComplaintTypeRoutingModule {}
