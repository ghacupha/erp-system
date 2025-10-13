///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { TransactionAccountComponent } from '../list/transaction-account.component';
import { TransactionAccountDetailComponent } from '../detail/transaction-account-detail.component';
import { TransactionAccountUpdateComponent } from '../update/transaction-account-update.component';
import { TransactionAccountRoutingResolveService } from './transaction-account-routing-resolve.service';

const transactionAccountRoute: Routes = [
  {
    path: '',
    component: TransactionAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransactionAccountDetailComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransactionAccountUpdateComponent,
    resolve: {
      transactionAccount: TransactionAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(transactionAccountRoute)],
  exports: [RouterModule],
})
export class TransactionAccountRoutingModule {}
