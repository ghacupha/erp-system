///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { LiabilityEnumerationComponent } from '../list/liability-enumeration.component';
import { LiabilityEnumerationUpdateComponent } from '../update/liability-enumeration-update.component';
import { PresentValueEnumerationComponent } from '../present-values/present-value-enumeration.component';

const routes: Routes = [
  {
    path: '',
    component: LiabilityEnumerationComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'], defaultSort: 'id,desc', },
  },
  {
    path: 'create',
    component: LiabilityEnumerationUpdateComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'] },
  },
  {
    path: ':id/present-values',
    component: PresentValueEnumerationComponent,
    canActivate: [UserRouteAccessService],
    data: { authorities: ['ROLE_LEASE_MANAGER'] },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LiabilityEnumerationRoutingModule {}
