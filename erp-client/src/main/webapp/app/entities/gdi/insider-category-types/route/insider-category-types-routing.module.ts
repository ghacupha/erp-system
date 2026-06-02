import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InsiderCategoryTypesComponent } from '../list/insider-category-types.component';
import { InsiderCategoryTypesDetailComponent } from '../detail/insider-category-types-detail.component';
import { InsiderCategoryTypesUpdateComponent } from '../update/insider-category-types-update.component';
import { InsiderCategoryTypesRoutingResolveService } from './insider-category-types-routing-resolve.service';

const insiderCategoryTypesRoute: Routes = [
  {
    path: '',
    component: InsiderCategoryTypesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsiderCategoryTypesDetailComponent,
    resolve: {
      insiderCategoryTypes: InsiderCategoryTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsiderCategoryTypesUpdateComponent,
    resolve: {
      insiderCategoryTypes: InsiderCategoryTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsiderCategoryTypesUpdateComponent,
    resolve: {
      insiderCategoryTypes: InsiderCategoryTypesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(insiderCategoryTypesRoute)],
  exports: [RouterModule],
})
export class InsiderCategoryTypesRoutingModule {}
