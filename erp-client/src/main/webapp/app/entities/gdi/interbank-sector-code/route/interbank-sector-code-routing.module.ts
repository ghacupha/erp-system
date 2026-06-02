import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InterbankSectorCodeComponent } from '../list/interbank-sector-code.component';
import { InterbankSectorCodeDetailComponent } from '../detail/interbank-sector-code-detail.component';
import { InterbankSectorCodeUpdateComponent } from '../update/interbank-sector-code-update.component';
import { InterbankSectorCodeRoutingResolveService } from './interbank-sector-code-routing-resolve.service';

const interbankSectorCodeRoute: Routes = [
  {
    path: '',
    component: InterbankSectorCodeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterbankSectorCodeDetailComponent,
    resolve: {
      interbankSectorCode: InterbankSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterbankSectorCodeUpdateComponent,
    resolve: {
      interbankSectorCode: InterbankSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterbankSectorCodeUpdateComponent,
    resolve: {
      interbankSectorCode: InterbankSectorCodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(interbankSectorCodeRoute)],
  exports: [RouterModule],
})
export class InterbankSectorCodeRoutingModule {}
