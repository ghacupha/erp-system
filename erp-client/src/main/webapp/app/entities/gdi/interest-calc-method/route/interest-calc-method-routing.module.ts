import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterestCalcMethodComponent } from '../list/interest-calc-method.component';
import { InterestCalcMethodDetailComponent } from '../detail/interest-calc-method-detail.component';
import { InterestCalcMethodUpdateComponent } from '../update/interest-calc-method-update.component';
import { InterestCalcMethodRoutingResolveService } from './interest-calc-method-routing-resolve.service';

const interestCalcMethodRoute: Routes = [
  {
    path: '',
    component: InterestCalcMethodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterestCalcMethodDetailComponent,
    resolve: {
      interestCalcMethod: InterestCalcMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterestCalcMethodUpdateComponent,
    resolve: {
      interestCalcMethod: InterestCalcMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterestCalcMethodUpdateComponent,
    resolve: {
      interestCalcMethod: InterestCalcMethodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interestCalcMethodRoute)],
  exports: [RouterModule],
})
export class InterestCalcMethodRoutingModule {}
