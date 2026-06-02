import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProcessStatusComponent } from '../list/process-status.component';
import { ProcessStatusDetailComponent } from '../detail/process-status-detail.component';
import { ProcessStatusUpdateComponent } from '../update/process-status-update.component';
import { ProcessStatusRoutingResolveService } from './process-status-routing-resolve.service';

const processStatusRoute: Routes = [
  {
    path: '',
    component: ProcessStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcessStatusDetailComponent,
    resolve: {
      processStatus: ProcessStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcessStatusUpdateComponent,
    resolve: {
      processStatus: ProcessStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcessStatusUpdateComponent,
    resolve: {
      processStatus: ProcessStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(processStatusRoute)],
  exports: [RouterModule],
})
export class ProcessStatusRoutingModule {}
