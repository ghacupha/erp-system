import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FiscalMonthComponent } from '../list/fiscal-month.component';
import { FiscalMonthDetailComponent } from '../detail/fiscal-month-detail.component';
import { FiscalMonthUpdateComponent } from '../update/fiscal-month-update.component';
import { FiscalMonthRoutingResolveService } from './fiscal-month-routing-resolve.service';

const fiscalMonthRoute: Routes = [
  {
    path: '',
    component: FiscalMonthComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FiscalMonthDetailComponent,
    resolve: {
      fiscalMonth: FiscalMonthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FiscalMonthUpdateComponent,
    resolve: {
      fiscalMonth: FiscalMonthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FiscalMonthUpdateComponent,
    resolve: {
      fiscalMonth: FiscalMonthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fiscalMonthRoute)],
  exports: [RouterModule],
})
export class FiscalMonthRoutingModule {}
