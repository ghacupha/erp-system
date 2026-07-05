import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountySubCountyCode } from '../county-sub-county-code.model';
import { CountySubCountyCodeService } from '../service/county-sub-county-code.service';

@Component({
  templateUrl: './county-sub-county-code-delete-dialog.component.html',
})
export class CountySubCountyCodeDeleteDialogComponent {
  countySubCountyCode?: ICountySubCountyCode;

  constructor(protected countySubCountyCodeService: CountySubCountyCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countySubCountyCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
