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
import { FiscalQuarterComponent } from '../list/fiscal-quarter.component';
import { FiscalQuarterDetailComponent } from '../detail/fiscal-quarter-detail.component';
import { FiscalQuarterUpdateComponent } from '../update/fiscal-quarter-update.component';
import { FiscalQuarterRoutingResolveService } from './fiscal-quarter-routing-resolve.service';

const fiscalQuarterRoute: Routes = [
  {
    path: '',
    component: FiscalQuarterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FiscalQuarterDetailComponent,
    resolve: {
      fiscalQuarter: FiscalQuarterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FiscalQuarterUpdateComponent,
    resolve: {
      fiscalQuarter: FiscalQuarterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FiscalQuarterUpdateComponent,
    resolve: {
      fiscalQuarter: FiscalQuarterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fiscalQuarterRoute)],
  exports: [RouterModule],
})
export class FiscalQuarterRoutingModule {}
