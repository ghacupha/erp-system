import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IssuersOfSecuritiesComponent } from '../list/issuers-of-securities.component';
import { IssuersOfSecuritiesDetailComponent } from '../detail/issuers-of-securities-detail.component';
import { IssuersOfSecuritiesUpdateComponent } from '../update/issuers-of-securities-update.component';
import { IssuersOfSecuritiesRoutingResolveService } from './issuers-of-securities-routing-resolve.service';

const issuersOfSecuritiesRoute: Routes = [
  {
    path: '',
    component: IssuersOfSecuritiesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IssuersOfSecuritiesDetailComponent,
    resolve: {
      issuersOfSecurities: IssuersOfSecuritiesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IssuersOfSecuritiesUpdateComponent,
    resolve: {
      issuersOfSecurities: IssuersOfSecuritiesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IssuersOfSecuritiesUpdateComponent,
    resolve: {
      issuersOfSecurities: IssuersOfSecuritiesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(issuersOfSecuritiesRoute)],
  exports: [RouterModule],
})
export class IssuersOfSecuritiesRoutingModule {}
