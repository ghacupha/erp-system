import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseTemplateComponent } from '../list/lease-template.component';
import { LeaseTemplateDetailComponent } from '../detail/lease-template-detail.component';
import { LeaseTemplateUpdateComponent } from '../update/lease-template-update.component';
import { LeaseTemplateRoutingResolveService } from './lease-template-routing-resolve.service';

const leaseTemplateRoute: Routes = [
  {
    path: '',
    component: LeaseTemplateComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseTemplateDetailComponent,
    resolve: {
      leaseTemplate: LeaseTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseTemplateUpdateComponent,
    resolve: {
      leaseTemplate: LeaseTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseTemplateUpdateComponent,
    resolve: {
      leaseTemplate: LeaseTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseTemplateRoute)],
  exports: [RouterModule],
})
export class LeaseTemplateRoutingModule {}
