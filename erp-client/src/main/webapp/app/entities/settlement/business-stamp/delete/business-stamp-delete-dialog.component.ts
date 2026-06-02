import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusinessStamp } from '../business-stamp.model';
import { BusinessStampService } from '../service/business-stamp.service';

@Component({
  templateUrl: './business-stamp-delete-dialog.component.html',
})
export class BusinessStampDeleteDialogComponent {
  businessStamp?: IBusinessStamp;

  constructor(protected businessStampService: BusinessStampService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessStampService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
