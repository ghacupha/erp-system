import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FraudTypeComponent } from '../list/fraud-type.component';
import { FraudTypeDetailComponent } from '../detail/fraud-type-detail.component';
import { FraudTypeUpdateComponent } from '../update/fraud-type-update.component';
import { FraudTypeRoutingResolveService } from './fraud-type-routing-resolve.service';

const fraudTypeRoute: Routes = [
  {
    path: '',
    component: FraudTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FraudTypeDetailComponent,
    resolve: {
      fraudType: FraudTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FraudTypeUpdateComponent,
    resolve: {
      fraudType: FraudTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FraudTypeUpdateComponent,
    resolve: {
      fraudType: FraudTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fraudTypeRoute)],
  exports: [RouterModule],
})
export class FraudTypeRoutingModule {}
