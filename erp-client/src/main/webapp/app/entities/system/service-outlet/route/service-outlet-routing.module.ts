import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceOutletComponent } from '../list/service-outlet.component';
import { ServiceOutletDetailComponent } from '../detail/service-outlet-detail.component';
import { ServiceOutletUpdateComponent } from '../update/service-outlet-update.component';
import { ServiceOutletRoutingResolveService } from './service-outlet-routing-resolve.service';

const serviceOutletRoute: Routes = [
  {
    path: '',
    component: ServiceOutletComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceOutletDetailComponent,
    resolve: {
      serviceOutlet: ServiceOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceOutletUpdateComponent,
    resolve: {
      serviceOutlet: ServiceOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceOutletUpdateComponent,
    resolve: {
      serviceOutlet: ServiceOutletRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceOutletRoute)],
  exports: [RouterModule],
})
export class ServiceOutletRoutingModule {}
