import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NetBookValueEntryComponent } from '../list/net-book-value-entry.component';
import { NetBookValueEntryDetailComponent } from '../detail/net-book-value-entry-detail.component';
import { NetBookValueEntryUpdateComponent } from '../update/net-book-value-entry-update.component';
import { NetBookValueEntryRoutingResolveService } from './net-book-value-entry-routing-resolve.service';

const netBookValueEntryRoute: Routes = [
  {
    path: '',
    component: NetBookValueEntryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NetBookValueEntryDetailComponent,
    resolve: {
      netBookValueEntry: NetBookValueEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NetBookValueEntryUpdateComponent,
    resolve: {
      netBookValueEntry: NetBookValueEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NetBookValueEntryUpdateComponent,
    resolve: {
      netBookValueEntry: NetBookValueEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(netBookValueEntryRoute)],
  exports: [RouterModule],
})
export class NetBookValueEntryRoutingModule {}
