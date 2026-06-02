import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RemittanceFlagComponent } from '../list/remittance-flag.component';
import { RemittanceFlagDetailComponent } from '../detail/remittance-flag-detail.component';
import { RemittanceFlagUpdateComponent } from '../update/remittance-flag-update.component';
import { RemittanceFlagRoutingResolveService } from './remittance-flag-routing-resolve.service';

const remittanceFlagRoute: Routes = [
  {
    path: '',
    component: RemittanceFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RemittanceFlagDetailComponent,
    resolve: {
      remittanceFlag: RemittanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RemittanceFlagUpdateComponent,
    resolve: {
      remittanceFlag: RemittanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RemittanceFlagUpdateComponent,
    resolve: {
      remittanceFlag: RemittanceFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(remittanceFlagRoute)],
  exports: [RouterModule],
})
export class RemittanceFlagRoutingModule {}
