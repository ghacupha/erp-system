import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyCodeComponent } from '../list/county-code.component';
import { CountyCodeDetailComponent } from '../detail/county-code-detail.component';
import { CountyCodeUpdateComponent } from '../update/county-code-update.component';
import { CountyCodeRoutingResolveService } from './county-code-routing-resolve.service';

const countyCodeRoute: Routes = [
  {
    path: '',
    component: CountyCodeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyCodeDetailComponent,
    resolve: {
      countyCode: CountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyCodeUpdateComponent,
    resolve: {
      countyCode: CountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyCodeUpdateComponent,
    resolve: {
      countyCode: CountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyCodeRoute)],
  exports: [RouterModule],
})
export class CountyCodeRoutingModule {}
