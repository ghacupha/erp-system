import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FiscalYearComponent } from '../list/fiscal-year.component';
import { FiscalYearDetailComponent } from '../detail/fiscal-year-detail.component';
import { FiscalYearUpdateComponent } from '../update/fiscal-year-update.component';
import { FiscalYearRoutingResolveService } from './fiscal-year-routing-resolve.service';

const fiscalYearRoute: Routes = [
  {
    path: '',
    component: FiscalYearComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FiscalYearDetailComponent,
    resolve: {
      fiscalYear: FiscalYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FiscalYearUpdateComponent,
    resolve: {
      fiscalYear: FiscalYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FiscalYearUpdateComponent,
    resolve: {
      fiscalYear: FiscalYearRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fiscalYearRoute)],
  exports: [RouterModule],
})
export class FiscalYearRoutingModule {}
