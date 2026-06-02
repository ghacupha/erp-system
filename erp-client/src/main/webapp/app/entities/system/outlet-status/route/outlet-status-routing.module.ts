import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OutletStatusComponent } from '../list/outlet-status.component';
import { OutletStatusDetailComponent } from '../detail/outlet-status-detail.component';
import { OutletStatusUpdateComponent } from '../update/outlet-status-update.component';
import { OutletStatusRoutingResolveService } from './outlet-status-routing-resolve.service';

const outletStatusRoute: Routes = [
  {
    path: '',
    component: OutletStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OutletStatusDetailComponent,
    resolve: {
      outletStatus: OutletStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OutletStatusUpdateComponent,
    resolve: {
      outletStatus: OutletStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OutletStatusUpdateComponent,
    resolve: {
      outletStatus: OutletStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(outletStatusRoute)],
  exports: [RouterModule],
})
export class OutletStatusRoutingModule {}
