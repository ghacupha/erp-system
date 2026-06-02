import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LegalStatusComponent } from '../list/legal-status.component';
import { LegalStatusDetailComponent } from '../detail/legal-status-detail.component';
import { LegalStatusUpdateComponent } from '../update/legal-status-update.component';
import { LegalStatusRoutingResolveService } from './legal-status-routing-resolve.service';

const legalStatusRoute: Routes = [
  {
    path: '',
    component: LegalStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LegalStatusDetailComponent,
    resolve: {
      legalStatus: LegalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LegalStatusUpdateComponent,
    resolve: {
      legalStatus: LegalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LegalStatusUpdateComponent,
    resolve: {
      legalStatus: LegalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(legalStatusRoute)],
  exports: [RouterModule],
})
export class LegalStatusRoutingModule {}
