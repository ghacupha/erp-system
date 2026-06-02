import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountType } from '../account-type.model';
import { AccountTypeService } from '../service/account-type.service';

@Component({
  templateUrl: './account-type-delete-dialog.component.html',
})
export class AccountTypeDeleteDialogComponent {
  accountType?: IAccountType;

  constructor(protected accountTypeService: AccountTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
