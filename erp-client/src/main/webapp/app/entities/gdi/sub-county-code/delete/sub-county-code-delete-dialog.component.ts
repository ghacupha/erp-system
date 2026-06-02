import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubCountyCode } from '../sub-county-code.model';
import { SubCountyCodeService } from '../service/sub-county-code.service';

@Component({
  templateUrl: './sub-county-code-delete-dialog.component.html',
})
export class SubCountyCodeDeleteDialogComponent {
  subCountyCode?: ISubCountyCode;

  constructor(protected subCountyCodeService: SubCountyCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subCountyCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
