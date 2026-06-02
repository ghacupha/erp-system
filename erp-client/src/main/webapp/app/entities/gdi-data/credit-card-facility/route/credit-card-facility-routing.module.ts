import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CreditCardFacilityComponent } from '../list/credit-card-facility.component';
import { CreditCardFacilityDetailComponent } from '../detail/credit-card-facility-detail.component';
import { CreditCardFacilityUpdateComponent } from '../update/credit-card-facility-update.component';
import { CreditCardFacilityRoutingResolveService } from './credit-card-facility-routing-resolve.service';

const creditCardFacilityRoute: Routes = [
  {
    path: '',
    component: CreditCardFacilityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CreditCardFacilityDetailComponent,
    resolve: {
      creditCardFacility: CreditCardFacilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CreditCardFacilityUpdateComponent,
    resolve: {
      creditCardFacility: CreditCardFacilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CreditCardFacilityUpdateComponent,
    resolve: {
      creditCardFacility: CreditCardFacilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(creditCardFacilityRoute)],
  exports: [RouterModule],
})
export class CreditCardFacilityRoutingModule {}
