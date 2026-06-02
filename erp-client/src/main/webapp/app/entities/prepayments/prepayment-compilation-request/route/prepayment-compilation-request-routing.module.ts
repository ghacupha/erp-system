import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentCompilationRequestComponent } from '../list/prepayment-compilation-request.component';
import { PrepaymentCompilationRequestDetailComponent } from '../detail/prepayment-compilation-request-detail.component';
import { PrepaymentCompilationRequestUpdateComponent } from '../update/prepayment-compilation-request-update.component';
import { PrepaymentCompilationRequestRoutingResolveService } from './prepayment-compilation-request-routing-resolve.service';

const prepaymentCompilationRequestRoute: Routes = [
  {
    path: '',
    component: PrepaymentCompilationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentCompilationRequestDetailComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentCompilationRequestUpdateComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentCompilationRequestUpdateComponent,
    resolve: {
      prepaymentCompilationRequest: PrepaymentCompilationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentCompilationRequestRoute)],
  exports: [RouterModule],
})
export class PrepaymentCompilationRequestRoutingModule {}
