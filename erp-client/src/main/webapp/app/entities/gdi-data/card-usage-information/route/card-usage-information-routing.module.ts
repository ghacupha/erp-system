import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CardUsageInformationComponent } from '../list/card-usage-information.component';
import { CardUsageInformationDetailComponent } from '../detail/card-usage-information-detail.component';
import { CardUsageInformationUpdateComponent } from '../update/card-usage-information-update.component';
import { CardUsageInformationRoutingResolveService } from './card-usage-information-routing-resolve.service';

const cardUsageInformationRoute: Routes = [
  {
    path: '',
    component: CardUsageInformationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CardUsageInformationDetailComponent,
    resolve: {
      cardUsageInformation: CardUsageInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CardUsageInformationUpdateComponent,
    resolve: {
      cardUsageInformation: CardUsageInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CardUsageInformationUpdateComponent,
    resolve: {
      cardUsageInformation: CardUsageInformationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cardUsageInformationRoute)],
  exports: [RouterModule],
})
export class CardUsageInformationRoutingModule {}
