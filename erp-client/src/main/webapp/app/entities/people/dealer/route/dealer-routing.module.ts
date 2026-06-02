import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DealerComponent } from '../list/dealer.component';
import { DealerDetailComponent } from '../detail/dealer-detail.component';
import { DealerUpdateComponent } from '../update/dealer-update.component';
import { DealerRoutingResolveService } from './dealer-routing-resolve.service';

const dealerRoute: Routes = [
  {
    path: '',
    component: DealerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DealerDetailComponent,
    resolve: {
      dealer: DealerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DealerUpdateComponent,
    resolve: {
      dealer: DealerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DealerUpdateComponent,
    resolve: {
      dealer: DealerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dealerRoute)],
  exports: [RouterModule],
})
export class DealerRoutingModule {}
