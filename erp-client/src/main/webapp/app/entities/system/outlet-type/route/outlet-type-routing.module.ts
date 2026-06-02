import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OutletTypeComponent } from '../list/outlet-type.component';
import { OutletTypeDetailComponent } from '../detail/outlet-type-detail.component';
import { OutletTypeUpdateComponent } from '../update/outlet-type-update.component';
import { OutletTypeRoutingResolveService } from './outlet-type-routing-resolve.service';

const outletTypeRoute: Routes = [
  {
    path: '',
    component: OutletTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OutletTypeDetailComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OutletTypeUpdateComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OutletTypeUpdateComponent,
    resolve: {
      outletType: OutletTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(outletTypeRoute)],
  exports: [RouterModule],
})
export class OutletTypeRoutingModule {}
