import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SecurityTenureComponent } from '../list/security-tenure.component';
import { SecurityTenureDetailComponent } from '../detail/security-tenure-detail.component';
import { SecurityTenureUpdateComponent } from '../update/security-tenure-update.component';
import { SecurityTenureRoutingResolveService } from './security-tenure-routing-resolve.service';

const securityTenureRoute: Routes = [
  {
    path: '',
    component: SecurityTenureComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityTenureDetailComponent,
    resolve: {
      securityTenure: SecurityTenureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityTenureUpdateComponent,
    resolve: {
      securityTenure: SecurityTenureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityTenureUpdateComponent,
    resolve: {
      securityTenure: SecurityTenureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(securityTenureRoute)],
  exports: [RouterModule],
})
export class SecurityTenureRoutingModule {}
