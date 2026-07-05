import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GdiTransactionDataIndexComponent } from '../list/gdi-transaction-data-index.component';
import { GdiTransactionDataIndexDetailComponent } from '../detail/gdi-transaction-data-index-detail.component';
import { GdiTransactionDataIndexUpdateComponent } from '../update/gdi-transaction-data-index-update.component';
import { GdiTransactionDataIndexRoutingResolveService } from './gdi-transaction-data-index-routing-resolve.service';

const gdiTransactionDataIndexRoute: Routes = [
  {
    path: '',
    component: GdiTransactionDataIndexComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GdiTransactionDataIndexDetailComponent,
    resolve: {
      gdiTransactionDataIndex: GdiTransactionDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GdiTransactionDataIndexUpdateComponent,
    resolve: {
      gdiTransactionDataIndex: GdiTransactionDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GdiTransactionDataIndexUpdateComponent,
    resolve: {
      gdiTransactionDataIndex: GdiTransactionDataIndexRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gdiTransactionDataIndexRoute)],
  exports: [RouterModule],
})
export class GdiTransactionDataIndexRoutingModule {}
