import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountySubCountyCodeComponent } from '../list/county-sub-county-code.component';
import { CountySubCountyCodeDetailComponent } from '../detail/county-sub-county-code-detail.component';
import { CountySubCountyCodeUpdateComponent } from '../update/county-sub-county-code-update.component';
import { CountySubCountyCodeRoutingResolveService } from './county-sub-county-code-routing-resolve.service';

const countySubCountyCodeRoute: Routes = [
  {
    path: '',
    component: CountySubCountyCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountySubCountyCodeDetailComponent,
    resolve: {
      countySubCountyCode: CountySubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountySubCountyCodeUpdateComponent,
    resolve: {
      countySubCountyCode: CountySubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountySubCountyCodeUpdateComponent,
    resolve: {
      countySubCountyCode: CountySubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countySubCountyCodeRoute)],
  exports: [RouterModule],
})
export class CountySubCountyCodeRoutingModule {}
