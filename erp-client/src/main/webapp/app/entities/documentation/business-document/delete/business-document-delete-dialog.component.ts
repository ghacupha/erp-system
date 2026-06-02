import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusinessDocument } from '../business-document.model';
import { BusinessDocumentService } from '../service/business-document.service';

@Component({
  templateUrl: './business-document-delete-dialog.component.html',
})
export class BusinessDocumentDeleteDialogComponent {
  businessDocument?: IBusinessDocument;

  constructor(protected businessDocumentService: BusinessDocumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessDocumentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
