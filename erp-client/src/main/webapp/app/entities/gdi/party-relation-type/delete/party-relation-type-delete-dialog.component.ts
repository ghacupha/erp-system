import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartyRelationType } from '../party-relation-type.model';
import { PartyRelationTypeService } from '../service/party-relation-type.service';

@Component({
  templateUrl: './party-relation-type-delete-dialog.component.html',
})
export class PartyRelationTypeDeleteDialogComponent {
  partyRelationType?: IPartyRelationType;

  constructor(protected partyRelationTypeService: PartyRelationTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partyRelationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
