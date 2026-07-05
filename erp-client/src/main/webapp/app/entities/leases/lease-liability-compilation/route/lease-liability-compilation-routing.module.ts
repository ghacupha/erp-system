import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaseLiabilityCompilationComponent } from '../list/lease-liability-compilation.component';
import { LeaseLiabilityCompilationDetailComponent } from '../detail/lease-liability-compilation-detail.component';
import { LeaseLiabilityCompilationUpdateComponent } from '../update/lease-liability-compilation-update.component';
import { LeaseLiabilityCompilationRoutingResolveService } from './lease-liability-compilation-routing-resolve.service';

const leaseLiabilityCompilationRoute: Routes = [
  {
    path: '',
    component: LeaseLiabilityCompilationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseLiabilityCompilationDetailComponent,
    resolve: {
      leaseLiabilityCompilation: LeaseLiabilityCompilationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseLiabilityCompilationUpdateComponent,
    resolve: {
      leaseLiabilityCompilation: LeaseLiabilityCompilationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseLiabilityCompilationUpdateComponent,
    resolve: {
      leaseLiabilityCompilation: LeaseLiabilityCompilationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaseLiabilityCompilationRoute)],
  exports: [RouterModule],
})
export class LeaseLiabilityCompilationRoutingModule {}
