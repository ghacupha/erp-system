import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbSubmittingInstitutionCategoryComponent } from '../list/crb-submitting-institution-category.component';
import { CrbSubmittingInstitutionCategoryDetailComponent } from '../detail/crb-submitting-institution-category-detail.component';
import { CrbSubmittingInstitutionCategoryUpdateComponent } from '../update/crb-submitting-institution-category-update.component';
import { CrbSubmittingInstitutionCategoryRoutingResolveService } from './crb-submitting-institution-category-routing-resolve.service';

const crbSubmittingInstitutionCategoryRoute: Routes = [
  {
    path: '',
    component: CrbSubmittingInstitutionCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbSubmittingInstitutionCategoryDetailComponent,
    resolve: {
      crbSubmittingInstitutionCategory: CrbSubmittingInstitutionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbSubmittingInstitutionCategoryUpdateComponent,
    resolve: {
      crbSubmittingInstitutionCategory: CrbSubmittingInstitutionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbSubmittingInstitutionCategoryUpdateComponent,
    resolve: {
      crbSubmittingInstitutionCategory: CrbSubmittingInstitutionCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbSubmittingInstitutionCategoryRoute)],
  exports: [RouterModule],
})
export class CrbSubmittingInstitutionCategoryRoutingModule {}
