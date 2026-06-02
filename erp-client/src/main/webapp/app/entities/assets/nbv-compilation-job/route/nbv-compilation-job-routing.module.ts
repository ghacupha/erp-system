import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NbvCompilationJobComponent } from '../list/nbv-compilation-job.component';
import { NbvCompilationJobDetailComponent } from '../detail/nbv-compilation-job-detail.component';
import { NbvCompilationJobUpdateComponent } from '../update/nbv-compilation-job-update.component';
import { NbvCompilationJobRoutingResolveService } from './nbv-compilation-job-routing-resolve.service';

const nbvCompilationJobRoute: Routes = [
  {
    path: '',
    component: NbvCompilationJobComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NbvCompilationJobDetailComponent,
    resolve: {
      nbvCompilationJob: NbvCompilationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NbvCompilationJobUpdateComponent,
    resolve: {
      nbvCompilationJob: NbvCompilationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NbvCompilationJobUpdateComponent,
    resolve: {
      nbvCompilationJob: NbvCompilationJobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nbvCompilationJobRoute)],
  exports: [RouterModule],
})
export class NbvCompilationJobRoutingModule {}
