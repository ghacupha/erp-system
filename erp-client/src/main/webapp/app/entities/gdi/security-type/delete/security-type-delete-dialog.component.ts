import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityType } from '../security-type.model';
import { SecurityTypeService } from '../service/security-type.service';

@Component({
  templateUrl: './security-type-delete-dialog.component.html',
})
export class SecurityTypeDeleteDialogComponent {
  securityType?: ISecurityType;

  constructor(protected securityTypeService: SecurityTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
