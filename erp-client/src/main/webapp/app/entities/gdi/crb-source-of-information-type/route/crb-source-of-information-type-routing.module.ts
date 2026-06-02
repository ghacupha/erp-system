import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbSourceOfInformationTypeComponent } from '../list/crb-source-of-information-type.component';
import { CrbSourceOfInformationTypeDetailComponent } from '../detail/crb-source-of-information-type-detail.component';
import { CrbSourceOfInformationTypeUpdateComponent } from '../update/crb-source-of-information-type-update.component';
import { CrbSourceOfInformationTypeRoutingResolveService } from './crb-source-of-information-type-routing-resolve.service';

const crbSourceOfInformationTypeRoute: Routes = [
  {
    path: '',
    component: CrbSourceOfInformationTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbSourceOfInformationTypeDetailComponent,
    resolve: {
      crbSourceOfInformationType: CrbSourceOfInformationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbSourceOfInformationTypeUpdateComponent,
    resolve: {
      crbSourceOfInformationType: CrbSourceOfInformationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbSourceOfInformationTypeUpdateComponent,
    resolve: {
      crbSourceOfInformationType: CrbSourceOfInformationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbSourceOfInformationTypeRoute)],
  exports: [RouterModule],
})
export class CrbSourceOfInformationTypeRoutingModule {}
