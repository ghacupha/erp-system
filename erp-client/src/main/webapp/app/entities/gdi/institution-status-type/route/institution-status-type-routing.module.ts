import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstitutionStatusTypeComponent } from '../list/institution-status-type.component';
import { InstitutionStatusTypeDetailComponent } from '../detail/institution-status-type-detail.component';
import { InstitutionStatusTypeUpdateComponent } from '../update/institution-status-type-update.component';
import { InstitutionStatusTypeRoutingResolveService } from './institution-status-type-routing-resolve.service';

const institutionStatusTypeRoute: Routes = [
  {
    path: '',
    component: InstitutionStatusTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstitutionStatusTypeDetailComponent,
    resolve: {
      institutionStatusType: InstitutionStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstitutionStatusTypeUpdateComponent,
    resolve: {
      institutionStatusType: InstitutionStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstitutionStatusTypeUpdateComponent,
    resolve: {
      institutionStatusType: InstitutionStatusTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(institutionStatusTypeRoute)],
  exports: [RouterModule],
})
export class InstitutionStatusTypeRoutingModule {}
