import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StaffCurrentEmploymentStatusComponent } from '../list/staff-current-employment-status.component';
import { StaffCurrentEmploymentStatusDetailComponent } from '../detail/staff-current-employment-status-detail.component';
import { StaffCurrentEmploymentStatusUpdateComponent } from '../update/staff-current-employment-status-update.component';
import { StaffCurrentEmploymentStatusRoutingResolveService } from './staff-current-employment-status-routing-resolve.service';

const staffCurrentEmploymentStatusRoute: Routes = [
  {
    path: '',
    component: StaffCurrentEmploymentStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StaffCurrentEmploymentStatusDetailComponent,
    resolve: {
      staffCurrentEmploymentStatus: StaffCurrentEmploymentStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StaffCurrentEmploymentStatusUpdateComponent,
    resolve: {
      staffCurrentEmploymentStatus: StaffCurrentEmploymentStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StaffCurrentEmploymentStatusUpdateComponent,
    resolve: {
      staffCurrentEmploymentStatus: StaffCurrentEmploymentStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(staffCurrentEmploymentStatusRoute)],
  exports: [RouterModule],
})
export class StaffCurrentEmploymentStatusRoutingModule {}
