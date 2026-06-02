import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JobSheetComponent } from '../list/job-sheet.component';
import { JobSheetDetailComponent } from '../detail/job-sheet-detail.component';
import { JobSheetUpdateComponent } from '../update/job-sheet-update.component';
import { JobSheetRoutingResolveService } from './job-sheet-routing-resolve.service';

const jobSheetRoute: Routes = [
  {
    path: '',
    component: JobSheetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JobSheetDetailComponent,
    resolve: {
      jobSheet: JobSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JobSheetUpdateComponent,
    resolve: {
      jobSheet: JobSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JobSheetUpdateComponent,
    resolve: {
      jobSheet: JobSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jobSheetRoute)],
  exports: [RouterModule],
})
export class JobSheetRoutingModule {}
