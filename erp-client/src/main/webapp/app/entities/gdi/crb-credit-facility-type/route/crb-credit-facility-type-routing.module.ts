import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CrbCreditFacilityTypeComponent } from '../list/crb-credit-facility-type.component';
import { CrbCreditFacilityTypeDetailComponent } from '../detail/crb-credit-facility-type-detail.component';
import { CrbCreditFacilityTypeUpdateComponent } from '../update/crb-credit-facility-type-update.component';
import { CrbCreditFacilityTypeRoutingResolveService } from './crb-credit-facility-type-routing-resolve.service';

const crbCreditFacilityTypeRoute: Routes = [
  {
    path: '',
    component: CrbCreditFacilityTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CrbCreditFacilityTypeDetailComponent,
    resolve: {
      crbCreditFacilityType: CrbCreditFacilityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CrbCreditFacilityTypeUpdateComponent,
    resolve: {
      crbCreditFacilityType: CrbCreditFacilityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CrbCreditFacilityTypeUpdateComponent,
    resolve: {
      crbCreditFacilityType: CrbCreditFacilityTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(crbCreditFacilityTypeRoute)],
  exports: [RouterModule],
})
export class CrbCreditFacilityTypeRoutingModule {}
