import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsoCurrencyCodeComponent } from '../list/iso-currency-code.component';
import { IsoCurrencyCodeDetailComponent } from '../detail/iso-currency-code-detail.component';
import { IsoCurrencyCodeUpdateComponent } from '../update/iso-currency-code-update.component';
import { IsoCurrencyCodeRoutingResolveService } from './iso-currency-code-routing-resolve.service';

const isoCurrencyCodeRoute: Routes = [
  {
    path: '',
    component: IsoCurrencyCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsoCurrencyCodeDetailComponent,
    resolve: {
      isoCurrencyCode: IsoCurrencyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsoCurrencyCodeUpdateComponent,
    resolve: {
      isoCurrencyCode: IsoCurrencyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsoCurrencyCodeUpdateComponent,
    resolve: {
      isoCurrencyCode: IsoCurrencyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isoCurrencyCodeRoute)],
  exports: [RouterModule],
})
export class IsoCurrencyCodeRoutingModule {}
