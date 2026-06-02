import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PurchaseOrderComponent } from '../list/purchase-order.component';
import { PurchaseOrderDetailComponent } from '../detail/purchase-order-detail.component';
import { PurchaseOrderUpdateComponent } from '../update/purchase-order-update.component';
import { PurchaseOrderRoutingResolveService } from './purchase-order-routing-resolve.service';

const purchaseOrderRoute: Routes = [
  {
    path: '',
    component: PurchaseOrderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchaseOrderDetailComponent,
    resolve: {
      purchaseOrder: PurchaseOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchaseOrderUpdateComponent,
    resolve: {
      purchaseOrder: PurchaseOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchaseOrderUpdateComponent,
    resolve: {
      purchaseOrder: PurchaseOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(purchaseOrderRoute)],
  exports: [RouterModule],
})
export class PurchaseOrderRoutingModule {}
