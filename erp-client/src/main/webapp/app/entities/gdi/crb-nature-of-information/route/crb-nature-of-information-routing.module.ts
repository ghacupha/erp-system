import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbNatureOfInformationComponent } from '../list/crb-nature-of-information.component';
import { CrbNatureOfInformationDetailComponent } from '../detail/crb-nature-of-information-detail.component';
import { CrbNatureOfInformationUpdateComponent } from '../update/crb-nature-of-information-update.component';
import { CrbNatureOfInformationRoutingResolveService } from './crb-nature-of-information-routing-resolve.service';

const crbNatureOfInformationRoute: Routes = [
  {
    path: '',
    component: CrbNatureOfInformationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbNatureOfInformationDetailComponent,
    resolve: {
      crbNatureOfInformation: CrbNatureOfInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbNatureOfInformationUpdateComponent,
    resolve: {
      crbNatureOfInformation: CrbNatureOfInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbNatureOfInformationUpdateComponent,
    resolve: {
      crbNatureOfInformation: CrbNatureOfInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbNatureOfInformationRoute)],
  exports: [RouterModule],
})
export class CrbNatureOfInformationRoutingModule {}
