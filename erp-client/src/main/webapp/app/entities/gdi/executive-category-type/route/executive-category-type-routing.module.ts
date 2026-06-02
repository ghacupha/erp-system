import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExecutiveCategoryTypeComponent } from '../list/executive-category-type.component';
import { ExecutiveCategoryTypeDetailComponent } from '../detail/executive-category-type-detail.component';
import { ExecutiveCategoryTypeUpdateComponent } from '../update/executive-category-type-update.component';
import { ExecutiveCategoryTypeRoutingResolveService } from './executive-category-type-routing-resolve.service';

const executiveCategoryTypeRoute: Routes = [
  {
    path: '',
    component: ExecutiveCategoryTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExecutiveCategoryTypeDetailComponent,
    resolve: {
      executiveCategoryType: ExecutiveCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExecutiveCategoryTypeUpdateComponent,
    resolve: {
      executiveCategoryType: ExecutiveCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExecutiveCategoryTypeUpdateComponent,
    resolve: {
      executiveCategoryType: ExecutiveCategoryTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(executiveCategoryTypeRoute)],
  exports: [RouterModule],
})
export class ExecutiveCategoryTypeRoutingModule {}
