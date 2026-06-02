import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcquiringIssuingFlagComponent } from '../list/acquiring-issuing-flag.component';
import { AcquiringIssuingFlagDetailComponent } from '../detail/acquiring-issuing-flag-detail.component';
import { AcquiringIssuingFlagUpdateComponent } from '../update/acquiring-issuing-flag-update.component';
import { AcquiringIssuingFlagRoutingResolveService } from './acquiring-issuing-flag-routing-resolve.service';

const acquiringIssuingFlagRoute: Routes = [
  {
    path: '',
    component: AcquiringIssuingFlagComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcquiringIssuingFlagDetailComponent,
    resolve: {
      acquiringIssuingFlag: AcquiringIssuingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcquiringIssuingFlagUpdateComponent,
    resolve: {
      acquiringIssuingFlag: AcquiringIssuingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcquiringIssuingFlagUpdateComponent,
    resolve: {
      acquiringIssuingFlag: AcquiringIssuingFlagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acquiringIssuingFlagRoute)],
  exports: [RouterModule],
})
export class AcquiringIssuingFlagRoutingModule {}
