import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbProductServiceFeeTypeComponent } from '../list/crb-product-service-fee-type.component';
import { CrbProductServiceFeeTypeDetailComponent } from '../detail/crb-product-service-fee-type-detail.component';
import { CrbProductServiceFeeTypeUpdateComponent } from '../update/crb-product-service-fee-type-update.component';
import { CrbProductServiceFeeTypeRoutingResolveService } from './crb-product-service-fee-type-routing-resolve.service';

const crbProductServiceFeeTypeRoute: Routes = [
  {
    path: '',
    component: CrbProductServiceFeeTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbProductServiceFeeTypeDetailComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbProductServiceFeeTypeUpdateComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbProductServiceFeeTypeUpdateComponent,
    resolve: {
      crbProductServiceFeeType: CrbProductServiceFeeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbProductServiceFeeTypeRoute)],
  exports: [RouterModule],
})
export class CrbProductServiceFeeTypeRoutingModule {}
