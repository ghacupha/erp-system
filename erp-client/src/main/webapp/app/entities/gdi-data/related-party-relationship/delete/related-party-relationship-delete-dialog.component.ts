import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRelatedPartyRelationship } from '../related-party-relationship.model';
import { RelatedPartyRelationshipService } from '../service/related-party-relationship.service';

@Component({
  templateUrl: './related-party-relationship-delete-dialog.component.html',
})
export class RelatedPartyRelationshipDeleteDialogComponent {
  relatedPartyRelationship?: IRelatedPartyRelationship;

  constructor(protected relatedPartyRelationshipService: RelatedPartyRelationshipService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.relatedPartyRelationshipService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
