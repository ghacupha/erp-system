import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyCode } from '../county-code.model';
import { CountyCodeService } from '../service/county-code.service';

@Component({
  templateUrl: './county-code-delete-dialog.component.html',
})
export class CountyCodeDeleteDialogComponent {
  countyCode?: ICountyCode;

  constructor(protected countyCodeService: CountyCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
