import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbNatureOfInformation } from '../crb-nature-of-information.model';
import { CrbNatureOfInformationService } from '../service/crb-nature-of-information.service';

@Component({
  templateUrl: './crb-nature-of-information-delete-dialog.component.html',
})
export class CrbNatureOfInformationDeleteDialogComponent {
  crbNatureOfInformation?: ICrbNatureOfInformation;

  constructor(protected crbNatureOfInformationService: CrbNatureOfInformationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbNatureOfInformationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
