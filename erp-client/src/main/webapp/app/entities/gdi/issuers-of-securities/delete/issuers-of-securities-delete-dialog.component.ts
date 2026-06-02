import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIssuersOfSecurities } from '../issuers-of-securities.model';
import { IssuersOfSecuritiesService } from '../service/issuers-of-securities.service';

@Component({
  templateUrl: './issuers-of-securities-delete-dialog.component.html',
})
export class IssuersOfSecuritiesDeleteDialogComponent {
  issuersOfSecurities?: IIssuersOfSecurities;

  constructor(protected issuersOfSecuritiesService: IssuersOfSecuritiesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.issuersOfSecuritiesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
