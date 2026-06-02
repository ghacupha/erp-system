import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepreciationBatchSequenceComponent } from '../list/depreciation-batch-sequence.component';
import { DepreciationBatchSequenceDetailComponent } from '../detail/depreciation-batch-sequence-detail.component';
import { DepreciationBatchSequenceUpdateComponent } from '../update/depreciation-batch-sequence-update.component';
import { DepreciationBatchSequenceRoutingResolveService } from './depreciation-batch-sequence-routing-resolve.service';

const depreciationBatchSequenceRoute: Routes = [
  {
    path: '',
    component: DepreciationBatchSequenceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepreciationBatchSequenceDetailComponent,
    resolve: {
      depreciationBatchSequence: DepreciationBatchSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepreciationBatchSequenceUpdateComponent,
    resolve: {
      depreciationBatchSequence: DepreciationBatchSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepreciationBatchSequenceUpdateComponent,
    resolve: {
      depreciationBatchSequence: DepreciationBatchSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depreciationBatchSequenceRoute)],
  exports: [RouterModule],
})
export class DepreciationBatchSequenceRoutingModule {}
