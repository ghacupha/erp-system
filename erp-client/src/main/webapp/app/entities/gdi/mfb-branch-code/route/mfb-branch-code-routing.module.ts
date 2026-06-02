import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MfbBranchCodeComponent } from '../list/mfb-branch-code.component';
import { MfbBranchCodeDetailComponent } from '../detail/mfb-branch-code-detail.component';
import { MfbBranchCodeUpdateComponent } from '../update/mfb-branch-code-update.component';
import { MfbBranchCodeRoutingResolveService } from './mfb-branch-code-routing-resolve.service';

const mfbBranchCodeRoute: Routes = [
  {
    path: '',
    component: MfbBranchCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MfbBranchCodeDetailComponent,
    resolve: {
      mfbBranchCode: MfbBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MfbBranchCodeUpdateComponent,
    resolve: {
      mfbBranchCode: MfbBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MfbBranchCodeUpdateComponent,
    resolve: {
      mfbBranchCode: MfbBranchCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mfbBranchCodeRoute)],
  exports: [RouterModule],
})
export class MfbBranchCodeRoutingModule {}
