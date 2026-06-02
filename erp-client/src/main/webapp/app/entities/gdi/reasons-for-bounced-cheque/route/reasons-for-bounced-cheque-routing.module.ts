import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReasonsForBouncedChequeComponent } from '../list/reasons-for-bounced-cheque.component';
import { ReasonsForBouncedChequeDetailComponent } from '../detail/reasons-for-bounced-cheque-detail.component';
import { ReasonsForBouncedChequeUpdateComponent } from '../update/reasons-for-bounced-cheque-update.component';
import { ReasonsForBouncedChequeRoutingResolveService } from './reasons-for-bounced-cheque-routing-resolve.service';

const reasonsForBouncedChequeRoute: Routes = [
  {
    path: '',
    component: ReasonsForBouncedChequeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReasonsForBouncedChequeDetailComponent,
    resolve: {
      reasonsForBouncedCheque: ReasonsForBouncedChequeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReasonsForBouncedChequeUpdateComponent,
    resolve: {
      reasonsForBouncedCheque: ReasonsForBouncedChequeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReasonsForBouncedChequeUpdateComponent,
    resolve: {
      reasonsForBouncedCheque: ReasonsForBouncedChequeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reasonsForBouncedChequeRoute)],
  exports: [RouterModule],
})
export class ReasonsForBouncedChequeRoutingModule {}
