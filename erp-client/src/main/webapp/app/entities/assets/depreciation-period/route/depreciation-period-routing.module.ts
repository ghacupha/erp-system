import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationPeriodComponent } from '../list/depreciation-period.component';
import { DepreciationPeriodDetailComponent } from '../detail/depreciation-period-detail.component';
import { DepreciationPeriodUpdateComponent } from '../update/depreciation-period-update.component';
import { DepreciationPeriodRoutingResolveService } from './depreciation-period-routing-resolve.service';

const depreciationPeriodRoute: Routes = [
  {
    path: '',
    component: DepreciationPeriodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationPeriodDetailComponent,
    resolve: {
      depreciationPeriod: DepreciationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationPeriodUpdateComponent,
    resolve: {
      depreciationPeriod: DepreciationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationPeriodUpdateComponent,
    resolve: {
      depreciationPeriod: DepreciationPeriodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationPeriodRoute)],
  exports: [RouterModule],
})
export class DepreciationPeriodRoutingModule {}
