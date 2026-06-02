import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SnaSectorCodeComponent } from '../list/sna-sector-code.component';
import { SnaSectorCodeDetailComponent } from '../detail/sna-sector-code-detail.component';
import { SnaSectorCodeUpdateComponent } from '../update/sna-sector-code-update.component';
import { SnaSectorCodeRoutingResolveService } from './sna-sector-code-routing-resolve.service';

const snaSectorCodeRoute: Routes = [
  {
    path: '',
    component: SnaSectorCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SnaSectorCodeDetailComponent,
    resolve: {
      snaSectorCode: SnaSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SnaSectorCodeUpdateComponent,
    resolve: {
      snaSectorCode: SnaSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SnaSectorCodeUpdateComponent,
    resolve: {
      snaSectorCode: SnaSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(snaSectorCodeRoute)],
  exports: [RouterModule],
})
export class SnaSectorCodeRoutingModule {}
