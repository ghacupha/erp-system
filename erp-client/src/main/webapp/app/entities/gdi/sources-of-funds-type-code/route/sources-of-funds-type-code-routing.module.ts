import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SourcesOfFundsTypeCodeComponent } from '../list/sources-of-funds-type-code.component';
import { SourcesOfFundsTypeCodeDetailComponent } from '../detail/sources-of-funds-type-code-detail.component';
import { SourcesOfFundsTypeCodeUpdateComponent } from '../update/sources-of-funds-type-code-update.component';
import { SourcesOfFundsTypeCodeRoutingResolveService } from './sources-of-funds-type-code-routing-resolve.service';

const sourcesOfFundsTypeCodeRoute: Routes = [
  {
    path: '',
    component: SourcesOfFundsTypeCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SourcesOfFundsTypeCodeDetailComponent,
    resolve: {
      sourcesOfFundsTypeCode: SourcesOfFundsTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SourcesOfFundsTypeCodeUpdateComponent,
    resolve: {
      sourcesOfFundsTypeCode: SourcesOfFundsTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SourcesOfFundsTypeCodeUpdateComponent,
    resolve: {
      sourcesOfFundsTypeCode: SourcesOfFundsTypeCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sourcesOfFundsTypeCodeRoute)],
  exports: [RouterModule],
})
export class SourcesOfFundsTypeCodeRoutingModule {}
