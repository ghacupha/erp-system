import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShareholderType } from '../shareholder-type.model';
import { ShareholderTypeService } from '../service/shareholder-type.service';

@Component({
  templateUrl: './shareholder-type-delete-dialog.component.html',
})
export class ShareholderTypeDeleteDialogComponent {
  shareholderType?: IShareholderType;

  constructor(protected shareholderTypeService: ShareholderTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shareholderTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
