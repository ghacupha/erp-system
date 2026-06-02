import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentMappingComponent } from '../list/prepayment-mapping.component';
import { PrepaymentMappingDetailComponent } from '../detail/prepayment-mapping-detail.component';
import { PrepaymentMappingUpdateComponent } from '../update/prepayment-mapping-update.component';
import { PrepaymentMappingRoutingResolveService } from './prepayment-mapping-routing-resolve.service';

const prepaymentMappingRoute: Routes = [
  {
    path: '',
    component: PrepaymentMappingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentMappingDetailComponent,
    resolve: {
      prepaymentMapping: PrepaymentMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentMappingUpdateComponent,
    resolve: {
      prepaymentMapping: PrepaymentMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentMappingUpdateComponent,
    resolve: {
      prepaymentMapping: PrepaymentMappingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentMappingRoute)],
  exports: [RouterModule],
})
export class PrepaymentMappingRoutingModule {}
