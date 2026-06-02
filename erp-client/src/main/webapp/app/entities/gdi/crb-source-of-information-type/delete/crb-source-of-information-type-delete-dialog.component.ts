import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbSourceOfInformationType } from '../crb-source-of-information-type.model';
import { CrbSourceOfInformationTypeService } from '../service/crb-source-of-information-type.service';

@Component({
  templateUrl: './crb-source-of-information-type-delete-dialog.component.html',
})
export class CrbSourceOfInformationTypeDeleteDialogComponent {
  crbSourceOfInformationType?: ICrbSourceOfInformationType;

  constructor(protected crbSourceOfInformationTypeService: CrbSourceOfInformationTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbSourceOfInformationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
