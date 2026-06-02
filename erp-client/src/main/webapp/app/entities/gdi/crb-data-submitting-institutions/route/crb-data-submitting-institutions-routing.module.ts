import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbDataSubmittingInstitutionsComponent } from '../list/crb-data-submitting-institutions.component';
import { CrbDataSubmittingInstitutionsDetailComponent } from '../detail/crb-data-submitting-institutions-detail.component';
import { CrbDataSubmittingInstitutionsUpdateComponent } from '../update/crb-data-submitting-institutions-update.component';
import { CrbDataSubmittingInstitutionsRoutingResolveService } from './crb-data-submitting-institutions-routing-resolve.service';

const crbDataSubmittingInstitutionsRoute: Routes = [
  {
    path: '',
    component: CrbDataSubmittingInstitutionsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbDataSubmittingInstitutionsDetailComponent,
    resolve: {
      crbDataSubmittingInstitutions: CrbDataSubmittingInstitutionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbDataSubmittingInstitutionsUpdateComponent,
    resolve: {
      crbDataSubmittingInstitutions: CrbDataSubmittingInstitutionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbDataSubmittingInstitutionsUpdateComponent,
    resolve: {
      crbDataSubmittingInstitutions: CrbDataSubmittingInstitutionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbDataSubmittingInstitutionsRoute)],
  exports: [RouterModule],
})
export class CrbDataSubmittingInstitutionsRoutingModule {}
