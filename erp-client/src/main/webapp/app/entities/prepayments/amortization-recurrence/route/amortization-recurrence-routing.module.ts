import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmortizationRecurrenceComponent } from '../list/amortization-recurrence.component';
import { AmortizationRecurrenceDetailComponent } from '../detail/amortization-recurrence-detail.component';
import { AmortizationRecurrenceUpdateComponent } from '../update/amortization-recurrence-update.component';
import { AmortizationRecurrenceRoutingResolveService } from './amortization-recurrence-routing-resolve.service';

const amortizationRecurrenceRoute: Routes = [
  {
    path: '',
    component: AmortizationRecurrenceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmortizationRecurrenceDetailComponent,
    resolve: {
      amortizationRecurrence: AmortizationRecurrenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmortizationRecurrenceUpdateComponent,
    resolve: {
      amortizationRecurrence: AmortizationRecurrenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmortizationRecurrenceUpdateComponent,
    resolve: {
      amortizationRecurrence: AmortizationRecurrenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amortizationRecurrenceRoute)],
  exports: [RouterModule],
})
export class AmortizationRecurrenceRoutingModule {}
