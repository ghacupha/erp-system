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
import { NbvCompilationBatchComponent } from '../list/nbv-compilation-batch.component';
import { NbvCompilationBatchDetailComponent } from '../detail/nbv-compilation-batch-detail.component';
import { NbvCompilationBatchUpdateComponent } from '../update/nbv-compilation-batch-update.component';
import { NbvCompilationBatchRoutingResolveService } from './nbv-compilation-batch-routing-resolve.service';

const nbvCompilationBatchRoute: Routes = [
  {
    path: '',
    component: NbvCompilationBatchComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NbvCompilationBatchDetailComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NbvCompilationBatchUpdateComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NbvCompilationBatchUpdateComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nbvCompilationBatchRoute)],
  exports: [RouterModule],
})
export class NbvCompilationBatchRoutingModule {}
