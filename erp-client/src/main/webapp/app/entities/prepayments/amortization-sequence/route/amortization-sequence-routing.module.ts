import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmortizationSequenceComponent } from '../list/amortization-sequence.component';
import { AmortizationSequenceDetailComponent } from '../detail/amortization-sequence-detail.component';
import { AmortizationSequenceUpdateComponent } from '../update/amortization-sequence-update.component';
import { AmortizationSequenceRoutingResolveService } from './amortization-sequence-routing-resolve.service';

const amortizationSequenceRoute: Routes = [
  {
    path: '',
    component: AmortizationSequenceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmortizationSequenceDetailComponent,
    resolve: {
      amortizationSequence: AmortizationSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmortizationSequenceUpdateComponent,
    resolve: {
      amortizationSequence: AmortizationSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmortizationSequenceUpdateComponent,
    resolve: {
      amortizationSequence: AmortizationSequenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amortizationSequenceRoute)],
  exports: [RouterModule],
})
export class AmortizationSequenceRoutingModule {}
