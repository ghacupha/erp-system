import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NbvCompilationBatchComponent } from '../list/nbv-compilation-batch.component';
import { NbvCompilationBatchDetailComponent } from '../detail/nbv-compilation-batch-detail.component';
import { NbvCompilationBatchUpdateComponent } from '../update/nbv-compilation-batch-update.component';
import { NbvCompilationBatchRoutingResolveService } from './nbv-compilation-batch-routing-resolve.service';

const nbvCompilationBatchRoute: Routes = [
  {
    path: '',
    component: NbvCompilationBatchComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NbvCompilationBatchDetailComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NbvCompilationBatchUpdateComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NbvCompilationBatchUpdateComponent,
    resolve: {
      nbvCompilationBatch: NbvCompilationBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nbvCompilationBatchRoute)],
  exports: [RouterModule],
})
export class NbvCompilationBatchRoutingModule {}
