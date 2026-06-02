import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkInProgressRegistrationComponent } from '../list/work-in-progress-registration.component';
import { WorkInProgressRegistrationDetailComponent } from '../detail/work-in-progress-registration-detail.component';
import { WorkInProgressRegistrationUpdateComponent } from '../update/work-in-progress-registration-update.component';
import { WorkInProgressRegistrationRoutingResolveService } from './work-in-progress-registration-routing-resolve.service';

const workInProgressRegistrationRoute: Routes = [
  {
    path: '',
    component: WorkInProgressRegistrationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkInProgressRegistrationDetailComponent,
    resolve: {
      workInProgressRegistration: WorkInProgressRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkInProgressRegistrationUpdateComponent,
    resolve: {
      workInProgressRegistration: WorkInProgressRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkInProgressRegistrationUpdateComponent,
    resolve: {
      workInProgressRegistration: WorkInProgressRegistrationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workInProgressRegistrationRoute)],
  exports: [RouterModule],
})
export class WorkInProgressRegistrationRoutingModule {}
