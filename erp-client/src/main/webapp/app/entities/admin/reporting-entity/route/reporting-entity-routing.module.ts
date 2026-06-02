import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportingEntityComponent } from '../list/reporting-entity.component';
import { ReportingEntityDetailComponent } from '../detail/reporting-entity-detail.component';
import { ReportingEntityUpdateComponent } from '../update/reporting-entity-update.component';
import { ReportingEntityRoutingResolveService } from './reporting-entity-routing-resolve.service';

const reportingEntityRoute: Routes = [
  {
    path: '',
    component: ReportingEntityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportingEntityDetailComponent,
    resolve: {
      reportingEntity: ReportingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportingEntityUpdateComponent,
    resolve: {
      reportingEntity: ReportingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportingEntityUpdateComponent,
    resolve: {
      reportingEntity: ReportingEntityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportingEntityRoute)],
  exports: [RouterModule],
})
export class ReportingEntityRoutingModule {}
