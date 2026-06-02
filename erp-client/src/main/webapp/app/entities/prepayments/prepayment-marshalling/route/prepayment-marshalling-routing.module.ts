import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrepaymentMarshallingComponent } from '../list/prepayment-marshalling.component';
import { PrepaymentMarshallingDetailComponent } from '../detail/prepayment-marshalling-detail.component';
import { PrepaymentMarshallingUpdateComponent } from '../update/prepayment-marshalling-update.component';
import { PrepaymentMarshallingRoutingResolveService } from './prepayment-marshalling-routing-resolve.service';

const prepaymentMarshallingRoute: Routes = [
  {
    path: '',
    component: PrepaymentMarshallingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrepaymentMarshallingDetailComponent,
    resolve: {
      prepaymentMarshalling: PrepaymentMarshallingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrepaymentMarshallingUpdateComponent,
    resolve: {
      prepaymentMarshalling: PrepaymentMarshallingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrepaymentMarshallingUpdateComponent,
    resolve: {
      prepaymentMarshalling: PrepaymentMarshallingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prepaymentMarshallingRoute)],
  exports: [RouterModule],
})
export class PrepaymentMarshallingRoutingModule {}
