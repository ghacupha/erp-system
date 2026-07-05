import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityTenure } from '../security-tenure.model';
import { SecurityTenureService } from '../service/security-tenure.service';

@Component({
  templateUrl: './security-tenure-delete-dialog.component.html',
})
export class SecurityTenureDeleteDialogComponent {
  securityTenure?: ISecurityTenure;

  constructor(protected securityTenureService: SecurityTenureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityTenureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
