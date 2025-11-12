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

import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../../../core/auth/user-route-access.service';
import { IFRS16LeaseContractRoutingResolveService } from './ifrs-16-lease-contract-routing-resolve.service';
import { IFRS16LeaseContractUpdateComponent } from '../update/ifrs-16-lease-contract-update.component';
import { NgModule } from '@angular/core';

const iFRS16LeaseContractCopyRoute: Routes = [
  {
    path: ':id/copy',
    component: IFRS16LeaseContractUpdateComponent,
    resolve: {
      iFRS16LeaseContract: IFRS16LeaseContractRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(iFRS16LeaseContractCopyRoute)],
  exports: [RouterModule],
})
export class Ifrs16LeaseContractRoutingCustomModule {
}
