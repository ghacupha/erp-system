import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubCountyCodeComponent } from '../list/sub-county-code.component';
import { SubCountyCodeDetailComponent } from '../detail/sub-county-code-detail.component';
import { SubCountyCodeUpdateComponent } from '../update/sub-county-code-update.component';
import { SubCountyCodeRoutingResolveService } from './sub-county-code-routing-resolve.service';

const subCountyCodeRoute: Routes = [
  {
    path: '',
    component: SubCountyCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubCountyCodeDetailComponent,
    resolve: {
      subCountyCode: SubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubCountyCodeUpdateComponent,
    resolve: {
      subCountyCode: SubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubCountyCodeUpdateComponent,
    resolve: {
      subCountyCode: SubCountyCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subCountyCodeRoute)],
  exports: [RouterModule],
})
export class SubCountyCodeRoutingModule {}
