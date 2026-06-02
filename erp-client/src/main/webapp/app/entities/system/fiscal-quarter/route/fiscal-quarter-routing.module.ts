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
