import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityClassificationType } from '../security-classification-type.model';
import { SecurityClassificationTypeService } from '../service/security-classification-type.service';

@Component({
  templateUrl: './security-classification-type-delete-dialog.component.html',
})
export class SecurityClassificationTypeDeleteDialogComponent {
  securityClassificationType?: ISecurityClassificationType;

  constructor(protected securityClassificationTypeService: SecurityClassificationTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityClassificationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
