import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TerminalsAndPOSComponent } from '../list/terminals-and-pos.component';
import { TerminalsAndPOSDetailComponent } from '../detail/terminals-and-pos-detail.component';
import { TerminalsAndPOSUpdateComponent } from '../update/terminals-and-pos-update.component';
import { TerminalsAndPOSRoutingResolveService } from './terminals-and-pos-routing-resolve.service';

const terminalsAndPOSRoute: Routes = [
  {
    path: '',
    component: TerminalsAndPOSComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TerminalsAndPOSDetailComponent,
    resolve: {
      terminalsAndPOS: TerminalsAndPOSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TerminalsAndPOSUpdateComponent,
    resolve: {
      terminalsAndPOS: TerminalsAndPOSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TerminalsAndPOSUpdateComponent,
    resolve: {
      terminalsAndPOS: TerminalsAndPOSRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(terminalsAndPOSRoute)],
  exports: [RouterModule],
})
export class TerminalsAndPOSRoutingModule {}
