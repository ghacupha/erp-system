import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShareHoldingFlagComponent } from '../list/share-holding-flag.component';
import { ShareHoldingFlagDetailComponent } from '../detail/share-holding-flag-detail.component';
import { ShareHoldingFlagUpdateComponent } from '../update/share-holding-flag-update.component';
import { ShareHoldingFlagRoutingResolveService } from './share-holding-flag-routing-resolve.service';

const shareHoldingFlagRoute: Routes = [
  {
    path: '',
    component: ShareHoldingFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShareHoldingFlagDetailComponent,
    resolve: {
      shareHoldingFlag: ShareHoldingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShareHoldingFlagUpdateComponent,
    resolve: {
      shareHoldingFlag: ShareHoldingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShareHoldingFlagUpdateComponent,
    resolve: {
      shareHoldingFlag: ShareHoldingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shareHoldingFlagRoute)],
  exports: [RouterModule],
})
export class ShareHoldingFlagRoutingModule {}
