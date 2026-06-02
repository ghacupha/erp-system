import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommitteeTypeComponent } from '../list/committee-type.component';
import { CommitteeTypeDetailComponent } from '../detail/committee-type-detail.component';
import { CommitteeTypeUpdateComponent } from '../update/committee-type-update.component';
import { CommitteeTypeRoutingResolveService } from './committee-type-routing-resolve.service';

const committeeTypeRoute: Routes = [
  {
    path: '',
    component: CommitteeTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommitteeTypeDetailComponent,
    resolve: {
      committeeType: CommitteeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommitteeTypeUpdateComponent,
    resolve: {
      committeeType: CommitteeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommitteeTypeUpdateComponent,
    resolve: {
      committeeType: CommitteeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(committeeTypeRoute)],
  exports: [RouterModule],
})
export class CommitteeTypeRoutingModule {}
