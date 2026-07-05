import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RelatedPartyRelationshipComponent } from '../list/related-party-relationship.component';
import { RelatedPartyRelationshipDetailComponent } from '../detail/related-party-relationship-detail.component';
import { RelatedPartyRelationshipUpdateComponent } from '../update/related-party-relationship-update.component';
import { RelatedPartyRelationshipRoutingResolveService } from './related-party-relationship-routing-resolve.service';

const relatedPartyRelationshipRoute: Routes = [
  {
    path: '',
    component: RelatedPartyRelationshipComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RelatedPartyRelationshipDetailComponent,
    resolve: {
      relatedPartyRelationship: RelatedPartyRelationshipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RelatedPartyRelationshipUpdateComponent,
    resolve: {
      relatedPartyRelationship: RelatedPartyRelationshipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RelatedPartyRelationshipUpdateComponent,
    resolve: {
      relatedPartyRelationship: RelatedPartyRelationshipRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(relatedPartyRelationshipRoute)],
  exports: [RouterModule],
})
export class RelatedPartyRelationshipRoutingModule {}
