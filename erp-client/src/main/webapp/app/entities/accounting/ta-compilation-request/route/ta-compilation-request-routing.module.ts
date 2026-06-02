import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TACompilationRequestComponent } from '../list/ta-compilation-request.component';
import { TACompilationRequestDetailComponent } from '../detail/ta-compilation-request-detail.component';
import { TACompilationRequestUpdateComponent } from '../update/ta-compilation-request-update.component';
import { TACompilationRequestRoutingResolveService } from './ta-compilation-request-routing-resolve.service';

const tACompilationRequestRoute: Routes = [
  {
    path: '',
    component: TACompilationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TACompilationRequestDetailComponent,
    resolve: {
      tACompilationRequest: TACompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TACompilationRequestUpdateComponent,
    resolve: {
      tACompilationRequest: TACompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TACompilationRequestUpdateComponent,
    resolve: {
      tACompilationRequest: TACompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tACompilationRequestRoute)],
  exports: [RouterModule],
})
export class TACompilationRequestRoutingModule {}
