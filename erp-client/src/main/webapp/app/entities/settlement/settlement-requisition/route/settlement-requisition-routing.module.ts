import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettlementRequisitionComponent } from '../list/settlement-requisition.component';
import { SettlementRequisitionDetailComponent } from '../detail/settlement-requisition-detail.component';
import { SettlementRequisitionUpdateComponent } from '../update/settlement-requisition-update.component';
import { SettlementRequisitionRoutingResolveService } from './settlement-requisition-routing-resolve.service';

const settlementRequisitionRoute: Routes = [
  {
    path: '',
    component: SettlementRequisitionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettlementRequisitionDetailComponent,
    resolve: {
      settlementRequisition: SettlementRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettlementRequisitionUpdateComponent,
    resolve: {
      settlementRequisition: SettlementRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettlementRequisitionUpdateComponent,
    resolve: {
      settlementRequisition: SettlementRequisitionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settlementRequisitionRoute)],
  exports: [RouterModule],
})
export class SettlementRequisitionRoutingModule {}
