import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentCategoryComponent } from '../list/payment-category.component';
import { PaymentCategoryDetailComponent } from '../detail/payment-category-detail.component';
import { PaymentCategoryUpdateComponent } from '../update/payment-category-update.component';
import { PaymentCategoryRoutingResolveService } from './payment-category-routing-resolve.service';

const paymentCategoryRoute: Routes = [
  {
    path: '',
    component: PaymentCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentCategoryDetailComponent,
    resolve: {
      paymentCategory: PaymentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentCategoryUpdateComponent,
    resolve: {
      paymentCategory: PaymentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentCategoryUpdateComponent,
    resolve: {
      paymentCategory: PaymentCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentCategoryRoute)],
  exports: [RouterModule],
})
export class PaymentCategoryRoutingModule {}
