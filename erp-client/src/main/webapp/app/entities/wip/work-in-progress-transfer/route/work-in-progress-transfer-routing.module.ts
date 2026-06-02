import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressTransferComponent } from '../list/work-in-progress-transfer.component';
import { WorkInProgressTransferDetailComponent } from '../detail/work-in-progress-transfer-detail.component';
import { WorkInProgressTransferUpdateComponent } from '../update/work-in-progress-transfer-update.component';
import { WorkInProgressTransferRoutingResolveService } from './work-in-progress-transfer-routing-resolve.service';

const workInProgressTransferRoute: Routes = [
  {
    path: '',
    component: WorkInProgressTransferComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressTransferDetailComponent,
    resolve: {
      workInProgressTransfer: WorkInProgressTransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkInProgressTransferUpdateComponent,
    resolve: {
      workInProgressTransfer: WorkInProgressTransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkInProgressTransferUpdateComponent,
    resolve: {
      workInProgressTransfer: WorkInProgressTransferRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressTransferRoute)],
  exports: [RouterModule],
})
export class WorkInProgressTransferRoutingModule {}
