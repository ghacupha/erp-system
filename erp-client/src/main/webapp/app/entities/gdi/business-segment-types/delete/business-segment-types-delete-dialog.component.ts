import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBusinessSegmentTypes } from '../business-segment-types.model';
import { BusinessSegmentTypesService } from '../service/business-segment-types.service';

@Component({
  templateUrl: './business-segment-types-delete-dialog.component.html',
})
export class BusinessSegmentTypesDeleteDialogComponent {
  businessSegmentTypes?: IBusinessSegmentTypes;

  constructor(protected businessSegmentTypesService: BusinessSegmentTypesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.businessSegmentTypesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
