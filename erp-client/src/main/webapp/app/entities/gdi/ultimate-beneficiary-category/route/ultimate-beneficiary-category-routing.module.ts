import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UltimateBeneficiaryCategoryComponent } from '../list/ultimate-beneficiary-category.component';
import { UltimateBeneficiaryCategoryDetailComponent } from '../detail/ultimate-beneficiary-category-detail.component';
import { UltimateBeneficiaryCategoryUpdateComponent } from '../update/ultimate-beneficiary-category-update.component';
import { UltimateBeneficiaryCategoryRoutingResolveService } from './ultimate-beneficiary-category-routing-resolve.service';

const ultimateBeneficiaryCategoryRoute: Routes = [
  {
    path: '',
    component: UltimateBeneficiaryCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UltimateBeneficiaryCategoryDetailComponent,
    resolve: {
      ultimateBeneficiaryCategory: UltimateBeneficiaryCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UltimateBeneficiaryCategoryUpdateComponent,
    resolve: {
      ultimateBeneficiaryCategory: UltimateBeneficiaryCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UltimateBeneficiaryCategoryUpdateComponent,
    resolve: {
      ultimateBeneficiaryCategory: UltimateBeneficiaryCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ultimateBeneficiaryCategoryRoute)],
  exports: [RouterModule],
})
export class UltimateBeneficiaryCategoryRoutingModule {}
