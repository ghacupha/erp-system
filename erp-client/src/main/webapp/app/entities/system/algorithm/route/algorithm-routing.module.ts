import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AlgorithmComponent } from '../list/algorithm.component';
import { AlgorithmDetailComponent } from '../detail/algorithm-detail.component';
import { AlgorithmUpdateComponent } from '../update/algorithm-update.component';
import { AlgorithmRoutingResolveService } from './algorithm-routing-resolve.service';

const algorithmRoute: Routes = [
  {
    path: '',
    component: AlgorithmComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlgorithmDetailComponent,
    resolve: {
      algorithm: AlgorithmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlgorithmUpdateComponent,
    resolve: {
      algorithm: AlgorithmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlgorithmUpdateComponent,
    resolve: {
      algorithm: AlgorithmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(algorithmRoute)],
  exports: [RouterModule],
})
export class AlgorithmRoutingModule {}
