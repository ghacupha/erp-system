import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsoCurrencyCode } from '../iso-currency-code.model';
import { IsoCurrencyCodeService } from '../service/iso-currency-code.service';

@Component({
  templateUrl: './iso-currency-code-delete-dialog.component.html',
})
export class IsoCurrencyCodeDeleteDialogComponent {
  isoCurrencyCode?: IIsoCurrencyCode;

  constructor(protected isoCurrencyCodeService: IsoCurrencyCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isoCurrencyCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
