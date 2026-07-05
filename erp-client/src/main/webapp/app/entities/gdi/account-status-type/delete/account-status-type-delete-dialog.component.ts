import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountStatusType } from '../account-status-type.model';
import { AccountStatusTypeService } from '../service/account-status-type.service';

@Component({
  templateUrl: './account-status-type-delete-dialog.component.html',
})
export class AccountStatusTypeDeleteDialogComponent {
  accountStatusType?: IAccountStatusType;

  constructor(protected accountStatusTypeService: AccountStatusTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountStatusTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
