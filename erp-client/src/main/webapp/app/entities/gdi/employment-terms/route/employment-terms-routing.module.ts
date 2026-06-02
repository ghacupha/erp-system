import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmploymentTermsComponent } from '../list/employment-terms.component';
import { EmploymentTermsDetailComponent } from '../detail/employment-terms-detail.component';
import { EmploymentTermsUpdateComponent } from '../update/employment-terms-update.component';
import { EmploymentTermsRoutingResolveService } from './employment-terms-routing-resolve.service';

const employmentTermsRoute: Routes = [
  {
    path: '',
    component: EmploymentTermsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmploymentTermsDetailComponent,
    resolve: {
      employmentTerms: EmploymentTermsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmploymentTermsUpdateComponent,
    resolve: {
      employmentTerms: EmploymentTermsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmploymentTermsUpdateComponent,
    resolve: {
      employmentTerms: EmploymentTermsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employmentTermsRoute)],
  exports: [RouterModule],
})
export class EmploymentTermsRoutingModule {}
