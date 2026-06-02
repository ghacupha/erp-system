import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PerformanceOfForeignSubsidiariesComponent } from '../list/performance-of-foreign-subsidiaries.component';
import { PerformanceOfForeignSubsidiariesDetailComponent } from '../detail/performance-of-foreign-subsidiaries-detail.component';
import { PerformanceOfForeignSubsidiariesUpdateComponent } from '../update/performance-of-foreign-subsidiaries-update.component';
import { PerformanceOfForeignSubsidiariesRoutingResolveService } from './performance-of-foreign-subsidiaries-routing-resolve.service';

const performanceOfForeignSubsidiariesRoute: Routes = [
  {
    path: '',
    component: PerformanceOfForeignSubsidiariesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PerformanceOfForeignSubsidiariesDetailComponent,
    resolve: {
      performanceOfForeignSubsidiaries: PerformanceOfForeignSubsidiariesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PerformanceOfForeignSubsidiariesUpdateComponent,
    resolve: {
      performanceOfForeignSubsidiaries: PerformanceOfForeignSubsidiariesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PerformanceOfForeignSubsidiariesUpdateComponent,
    resolve: {
      performanceOfForeignSubsidiaries: PerformanceOfForeignSubsidiariesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(performanceOfForeignSubsidiariesRoute)],
  exports: [RouterModule],
})
export class PerformanceOfForeignSubsidiariesRoutingModule {}
