import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbAgingBandsComponent } from '../list/crb-aging-bands.component';
import { CrbAgingBandsDetailComponent } from '../detail/crb-aging-bands-detail.component';
import { CrbAgingBandsUpdateComponent } from '../update/crb-aging-bands-update.component';
import { CrbAgingBandsRoutingResolveService } from './crb-aging-bands-routing-resolve.service';

const crbAgingBandsRoute: Routes = [
  {
    path: '',
    component: CrbAgingBandsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbAgingBandsDetailComponent,
    resolve: {
      crbAgingBands: CrbAgingBandsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbAgingBandsUpdateComponent,
    resolve: {
      crbAgingBands: CrbAgingBandsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbAgingBandsUpdateComponent,
    resolve: {
      crbAgingBands: CrbAgingBandsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbAgingBandsRoute)],
  exports: [RouterModule],
})
export class CrbAgingBandsRoutingModule {}
