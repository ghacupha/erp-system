import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountAttributeMetadata } from '../account-attribute-metadata.model';
import { AccountAttributeMetadataService } from '../service/account-attribute-metadata.service';

@Component({
  templateUrl: './account-attribute-metadata-delete-dialog.component.html',
})
export class AccountAttributeMetadataDeleteDialogComponent {
  accountAttributeMetadata?: IAccountAttributeMetadata;

  constructor(protected accountAttributeMetadataService: AccountAttributeMetadataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountAttributeMetadataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
