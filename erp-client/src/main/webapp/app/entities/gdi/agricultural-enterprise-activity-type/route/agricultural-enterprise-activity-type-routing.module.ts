import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgriculturalEnterpriseActivityTypeComponent } from '../list/agricultural-enterprise-activity-type.component';
import { AgriculturalEnterpriseActivityTypeDetailComponent } from '../detail/agricultural-enterprise-activity-type-detail.component';
import { AgriculturalEnterpriseActivityTypeUpdateComponent } from '../update/agricultural-enterprise-activity-type-update.component';
import { AgriculturalEnterpriseActivityTypeRoutingResolveService } from './agricultural-enterprise-activity-type-routing-resolve.service';

const agriculturalEnterpriseActivityTypeRoute: Routes = [
  {
    path: '',
    component: AgriculturalEnterpriseActivityTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgriculturalEnterpriseActivityTypeDetailComponent,
    resolve: {
      agriculturalEnterpriseActivityType: AgriculturalEnterpriseActivityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgriculturalEnterpriseActivityTypeUpdateComponent,
    resolve: {
      agriculturalEnterpriseActivityType: AgriculturalEnterpriseActivityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgriculturalEnterpriseActivityTypeUpdateComponent,
    resolve: {
      agriculturalEnterpriseActivityType: AgriculturalEnterpriseActivityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(agriculturalEnterpriseActivityTypeRoute)],
  exports: [RouterModule],
})
export class AgriculturalEnterpriseActivityTypeRoutingModule {}
