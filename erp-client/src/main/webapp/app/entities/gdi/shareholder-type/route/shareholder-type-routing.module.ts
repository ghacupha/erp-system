import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShareholderTypeComponent } from '../list/shareholder-type.component';
import { ShareholderTypeDetailComponent } from '../detail/shareholder-type-detail.component';
import { ShareholderTypeUpdateComponent } from '../update/shareholder-type-update.component';
import { ShareholderTypeRoutingResolveService } from './shareholder-type-routing-resolve.service';

const shareholderTypeRoute: Routes = [
  {
    path: '',
    component: ShareholderTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShareholderTypeDetailComponent,
    resolve: {
      shareholderType: ShareholderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShareholderTypeUpdateComponent,
    resolve: {
      shareholderType: ShareholderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShareholderTypeUpdateComponent,
    resolve: {
      shareholderType: ShareholderTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shareholderTypeRoute)],
  exports: [RouterModule],
})
export class ShareholderTypeRoutingModule {}
