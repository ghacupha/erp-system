import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartyRelationTypeComponent } from '../list/party-relation-type.component';
import { PartyRelationTypeDetailComponent } from '../detail/party-relation-type-detail.component';
import { PartyRelationTypeUpdateComponent } from '../update/party-relation-type-update.component';
import { PartyRelationTypeRoutingResolveService } from './party-relation-type-routing-resolve.service';

const partyRelationTypeRoute: Routes = [
  {
    path: '',
    component: PartyRelationTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartyRelationTypeDetailComponent,
    resolve: {
      partyRelationType: PartyRelationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartyRelationTypeUpdateComponent,
    resolve: {
      partyRelationType: PartyRelationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartyRelationTypeUpdateComponent,
    resolve: {
      partyRelationType: PartyRelationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partyRelationTypeRoute)],
  exports: [RouterModule],
})
export class PartyRelationTypeRoutingModule {}
