import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsoCountryCodeComponent } from '../list/iso-country-code.component';
import { IsoCountryCodeDetailComponent } from '../detail/iso-country-code-detail.component';
import { IsoCountryCodeUpdateComponent } from '../update/iso-country-code-update.component';
import { IsoCountryCodeRoutingResolveService } from './iso-country-code-routing-resolve.service';

const isoCountryCodeRoute: Routes = [
  {
    path: '',
    component: IsoCountryCodeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsoCountryCodeDetailComponent,
    resolve: {
      isoCountryCode: IsoCountryCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsoCountryCodeUpdateComponent,
    resolve: {
      isoCountryCode: IsoCountryCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsoCountryCodeUpdateComponent,
    resolve: {
      isoCountryCode: IsoCountryCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isoCountryCodeRoute)],
  exports: [RouterModule],
})
export class IsoCountryCodeRoutingModule {}
