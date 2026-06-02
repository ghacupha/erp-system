import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MerchantTypeComponent } from '../list/merchant-type.component';
import { MerchantTypeDetailComponent } from '../detail/merchant-type-detail.component';
import { MerchantTypeUpdateComponent } from '../update/merchant-type-update.component';
import { MerchantTypeRoutingResolveService } from './merchant-type-routing-resolve.service';

const merchantTypeRoute: Routes = [
  {
    path: '',
    component: MerchantTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MerchantTypeDetailComponent,
    resolve: {
      merchantType: MerchantTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MerchantTypeUpdateComponent,
    resolve: {
      merchantType: MerchantTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MerchantTypeUpdateComponent,
    resolve: {
      merchantType: MerchantTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(merchantTypeRoute)],
  exports: [RouterModule],
})
export class MerchantTypeRoutingModule {}
