import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstitutionContactDetails } from '../institution-contact-details.model';
import { InstitutionContactDetailsService } from '../service/institution-contact-details.service';

@Component({
  templateUrl: './institution-contact-details-delete-dialog.component.html',
})
export class InstitutionContactDetailsDeleteDialogComponent {
  institutionContactDetails?: IInstitutionContactDetails;

  constructor(protected institutionContactDetailsService: InstitutionContactDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.institutionContactDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
