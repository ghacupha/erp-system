import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbAgentServiceTypeComponent } from '../list/crb-agent-service-type.component';
import { CrbAgentServiceTypeDetailComponent } from '../detail/crb-agent-service-type-detail.component';
import { CrbAgentServiceTypeUpdateComponent } from '../update/crb-agent-service-type-update.component';
import { CrbAgentServiceTypeRoutingResolveService } from './crb-agent-service-type-routing-resolve.service';

const crbAgentServiceTypeRoute: Routes = [
  {
    path: '',
    component: CrbAgentServiceTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbAgentServiceTypeDetailComponent,
    resolve: {
      crbAgentServiceType: CrbAgentServiceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbAgentServiceTypeUpdateComponent,
    resolve: {
      crbAgentServiceType: CrbAgentServiceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbAgentServiceTypeUpdateComponent,
    resolve: {
      crbAgentServiceType: CrbAgentServiceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbAgentServiceTypeRoute)],
  exports: [RouterModule],
})
export class CrbAgentServiceTypeRoutingModule {}
