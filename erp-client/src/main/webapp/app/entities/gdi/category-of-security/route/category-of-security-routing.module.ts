import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoryOfSecurityComponent } from '../list/category-of-security.component';
import { CategoryOfSecurityDetailComponent } from '../detail/category-of-security-detail.component';
import { CategoryOfSecurityUpdateComponent } from '../update/category-of-security-update.component';
import { CategoryOfSecurityRoutingResolveService } from './category-of-security-routing-resolve.service';

const categoryOfSecurityRoute: Routes = [
  {
    path: '',
    component: CategoryOfSecurityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoryOfSecurityDetailComponent,
    resolve: {
      categoryOfSecurity: CategoryOfSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoryOfSecurityUpdateComponent,
    resolve: {
      categoryOfSecurity: CategoryOfSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoryOfSecurityUpdateComponent,
    resolve: {
      categoryOfSecurity: CategoryOfSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoryOfSecurityRoute)],
  exports: [RouterModule],
})
export class CategoryOfSecurityRoutingModule {}
