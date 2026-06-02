import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FxTransactionRateTypeComponent } from '../list/fx-transaction-rate-type.component';
import { FxTransactionRateTypeDetailComponent } from '../detail/fx-transaction-rate-type-detail.component';
import { FxTransactionRateTypeUpdateComponent } from '../update/fx-transaction-rate-type-update.component';
import { FxTransactionRateTypeRoutingResolveService } from './fx-transaction-rate-type-routing-resolve.service';

const fxTransactionRateTypeRoute: Routes = [
  {
    path: '',
    component: FxTransactionRateTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FxTransactionRateTypeDetailComponent,
    resolve: {
      fxTransactionRateType: FxTransactionRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FxTransactionRateTypeUpdateComponent,
    resolve: {
      fxTransactionRateType: FxTransactionRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FxTransactionRateTypeUpdateComponent,
    resolve: {
      fxTransactionRateType: FxTransactionRateTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fxTransactionRateTypeRoute)],
  exports: [RouterModule],
})
export class FxTransactionRateTypeRoutingModule {}
