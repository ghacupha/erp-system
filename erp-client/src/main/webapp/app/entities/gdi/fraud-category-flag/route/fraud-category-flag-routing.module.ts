import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FraudCategoryFlagComponent } from '../list/fraud-category-flag.component';
import { FraudCategoryFlagDetailComponent } from '../detail/fraud-category-flag-detail.component';
import { FraudCategoryFlagUpdateComponent } from '../update/fraud-category-flag-update.component';
import { FraudCategoryFlagRoutingResolveService } from './fraud-category-flag-routing-resolve.service';

const fraudCategoryFlagRoute: Routes = [
  {
    path: '',
    component: FraudCategoryFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FraudCategoryFlagDetailComponent,
    resolve: {
      fraudCategoryFlag: FraudCategoryFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FraudCategoryFlagUpdateComponent,
    resolve: {
      fraudCategoryFlag: FraudCategoryFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FraudCategoryFlagUpdateComponent,
    resolve: {
      fraudCategoryFlag: FraudCategoryFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fraudCategoryFlagRoute)],
  exports: [RouterModule],
})
export class FraudCategoryFlagRoutingModule {}
