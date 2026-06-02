import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityClearance } from '../security-clearance.model';
import { SecurityClearanceService } from '../service/security-clearance.service';

@Component({
  templateUrl: './security-clearance-delete-dialog.component.html',
})
export class SecurityClearanceDeleteDialogComponent {
  securityClearance?: ISecurityClearance;

  constructor(protected securityClearanceService: SecurityClearanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityClearanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
