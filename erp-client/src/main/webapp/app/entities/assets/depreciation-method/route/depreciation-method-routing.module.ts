import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationMethodComponent } from '../list/depreciation-method.component';
import { DepreciationMethodDetailComponent } from '../detail/depreciation-method-detail.component';
import { DepreciationMethodUpdateComponent } from '../update/depreciation-method-update.component';
import { DepreciationMethodRoutingResolveService } from './depreciation-method-routing-resolve.service';

const depreciationMethodRoute: Routes = [
  {
    path: '',
    component: DepreciationMethodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationMethodDetailComponent,
    resolve: {
      depreciationMethod: DepreciationMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationMethodUpdateComponent,
    resolve: {
      depreciationMethod: DepreciationMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationMethodUpdateComponent,
    resolve: {
      depreciationMethod: DepreciationMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationMethodRoute)],
  exports: [RouterModule],
})
export class DepreciationMethodRoutingModule {}
