import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlaceholderComponent } from '../list/placeholder.component';
import { PlaceholderDetailComponent } from '../detail/placeholder-detail.component';
import { PlaceholderUpdateComponent } from '../update/placeholder-update.component';
import { PlaceholderRoutingResolveService } from './placeholder-routing-resolve.service';

const placeholderRoute: Routes = [
  {
    path: '',
    component: PlaceholderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlaceholderDetailComponent,
    resolve: {
      placeholder: PlaceholderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlaceholderUpdateComponent,
    resolve: {
      placeholder: PlaceholderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlaceholderUpdateComponent,
    resolve: {
      placeholder: PlaceholderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(placeholderRoute)],
  exports: [RouterModule],
})
export class PlaceholderRoutingModule {}
