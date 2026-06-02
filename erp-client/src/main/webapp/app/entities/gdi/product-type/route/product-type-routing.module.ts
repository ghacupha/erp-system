import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductTypeComponent } from '../list/product-type.component';
import { ProductTypeDetailComponent } from '../detail/product-type-detail.component';
import { ProductTypeUpdateComponent } from '../update/product-type-update.component';
import { ProductTypeRoutingResolveService } from './product-type-routing-resolve.service';

const productTypeRoute: Routes = [
  {
    path: '',
    component: ProductTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductTypeDetailComponent,
    resolve: {
      productType: ProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productTypeRoute)],
  exports: [RouterModule],
})
export class ProductTypeRoutingModule {}
