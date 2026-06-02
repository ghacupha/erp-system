import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManagementMemberType } from '../management-member-type.model';
import { ManagementMemberTypeService } from '../service/management-member-type.service';

@Component({
  templateUrl: './management-member-type-delete-dialog.component.html',
})
export class ManagementMemberTypeDeleteDialogComponent {
  managementMemberType?: IManagementMemberType;

  constructor(protected managementMemberTypeService: ManagementMemberTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.managementMemberTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
