import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbAmountCategoryBandComponent } from '../list/crb-amount-category-band.component';
import { CrbAmountCategoryBandDetailComponent } from '../detail/crb-amount-category-band-detail.component';
import { CrbAmountCategoryBandUpdateComponent } from '../update/crb-amount-category-band-update.component';
import { CrbAmountCategoryBandRoutingResolveService } from './crb-amount-category-band-routing-resolve.service';

const crbAmountCategoryBandRoute: Routes = [
  {
    path: '',
    component: CrbAmountCategoryBandComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbAmountCategoryBandDetailComponent,
    resolve: {
      crbAmountCategoryBand: CrbAmountCategoryBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbAmountCategoryBandUpdateComponent,
    resolve: {
      crbAmountCategoryBand: CrbAmountCategoryBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbAmountCategoryBandUpdateComponent,
    resolve: {
      crbAmountCategoryBand: CrbAmountCategoryBandRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbAmountCategoryBandRoute)],
  exports: [RouterModule],
})
export class CrbAmountCategoryBandRoutingModule {}
