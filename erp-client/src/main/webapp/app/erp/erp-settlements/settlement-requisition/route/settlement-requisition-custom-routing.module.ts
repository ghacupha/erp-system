///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettlementRequisitionUpdateComponent } from '../update/settlement-requisition-update.component';
import { SettlementRequisitionRoutingResolveService } from './settlement-requisition-routing-resolve.service';

/**
 * Custom navigation when copy data between instances of the same entity. The settlement-requisition
 * is intended to be mostly immutable, in that you can create it or delete it but not modify it.
 */
const settlementRequisitionRoute: Routes = [
  {
    path: ':id/copy',
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
export class SettlementRequisitionCustomRoutingModule {}
