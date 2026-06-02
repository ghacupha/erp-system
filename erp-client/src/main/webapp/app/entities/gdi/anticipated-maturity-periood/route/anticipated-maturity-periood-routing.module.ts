import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnticipatedMaturityPerioodComponent } from '../list/anticipated-maturity-periood.component';
import { AnticipatedMaturityPerioodDetailComponent } from '../detail/anticipated-maturity-periood-detail.component';
import { AnticipatedMaturityPerioodUpdateComponent } from '../update/anticipated-maturity-periood-update.component';
import { AnticipatedMaturityPerioodRoutingResolveService } from './anticipated-maturity-periood-routing-resolve.service';

const anticipatedMaturityPerioodRoute: Routes = [
  {
    path: '',
    component: AnticipatedMaturityPerioodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnticipatedMaturityPerioodDetailComponent,
    resolve: {
      anticipatedMaturityPeriood: AnticipatedMaturityPerioodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnticipatedMaturityPerioodUpdateComponent,
    resolve: {
      anticipatedMaturityPeriood: AnticipatedMaturityPerioodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnticipatedMaturityPerioodUpdateComponent,
    resolve: {
      anticipatedMaturityPeriood: AnticipatedMaturityPerioodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(anticipatedMaturityPerioodRoute)],
  exports: [RouterModule],
})
export class AnticipatedMaturityPerioodRoutingModule {}
