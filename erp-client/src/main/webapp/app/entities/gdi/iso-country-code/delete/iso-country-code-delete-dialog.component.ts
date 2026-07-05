import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIsoCountryCode } from '../iso-country-code.model';
import { IsoCountryCodeService } from '../service/iso-country-code.service';

@Component({
  templateUrl: './iso-country-code-delete-dialog.component.html',
})
export class IsoCountryCodeDeleteDialogComponent {
  isoCountryCode?: IIsoCountryCode;

  constructor(protected isoCountryCodeService: IsoCountryCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.isoCountryCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
