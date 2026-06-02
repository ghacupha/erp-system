import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstitutionCodeComponent } from '../list/institution-code.component';
import { InstitutionCodeDetailComponent } from '../detail/institution-code-detail.component';
import { InstitutionCodeUpdateComponent } from '../update/institution-code-update.component';
import { InstitutionCodeRoutingResolveService } from './institution-code-routing-resolve.service';

const institutionCodeRoute: Routes = [
  {
    path: '',
    component: InstitutionCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstitutionCodeDetailComponent,
    resolve: {
      institutionCode: InstitutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstitutionCodeUpdateComponent,
    resolve: {
      institutionCode: InstitutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstitutionCodeUpdateComponent,
    resolve: {
      institutionCode: InstitutionCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(institutionCodeRoute)],
  exports: [RouterModule],
})
export class InstitutionCodeRoutingModule {}
