import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SecurityClassificationTypeComponent } from '../list/security-classification-type.component';
import { SecurityClassificationTypeDetailComponent } from '../detail/security-classification-type-detail.component';
import { SecurityClassificationTypeUpdateComponent } from '../update/security-classification-type-update.component';
import { SecurityClassificationTypeRoutingResolveService } from './security-classification-type-routing-resolve.service';

const securityClassificationTypeRoute: Routes = [
  {
    path: '',
    component: SecurityClassificationTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityClassificationTypeDetailComponent,
    resolve: {
      securityClassificationType: SecurityClassificationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityClassificationTypeUpdateComponent,
    resolve: {
      securityClassificationType: SecurityClassificationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityClassificationTypeUpdateComponent,
    resolve: {
      securityClassificationType: SecurityClassificationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(securityClassificationTypeRoute)],
  exports: [RouterModule],
})
export class SecurityClassificationTypeRoutingModule {}
