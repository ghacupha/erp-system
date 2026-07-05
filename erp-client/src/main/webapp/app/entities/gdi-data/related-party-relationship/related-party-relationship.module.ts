import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RelatedPartyRelationshipComponent } from './list/related-party-relationship.component';
import { RelatedPartyRelationshipDetailComponent } from './detail/related-party-relationship-detail.component';
import { RelatedPartyRelationshipUpdateComponent } from './update/related-party-relationship-update.component';
import { RelatedPartyRelationshipDeleteDialogComponent } from './delete/related-party-relationship-delete-dialog.component';
import { RelatedPartyRelationshipRoutingModule } from './route/related-party-relationship-routing.module';

@NgModule({
  imports: [SharedModule, RelatedPartyRelationshipRoutingModule],
  declarations: [
    RelatedPartyRelationshipComponent,
    RelatedPartyRelationshipDetailComponent,
    RelatedPartyRelationshipUpdateComponent,
    RelatedPartyRelationshipDeleteDialogComponent,
  ],
  entryComponents: [RelatedPartyRelationshipDeleteDialogComponent],
})
export class RelatedPartyRelationshipModule {}
