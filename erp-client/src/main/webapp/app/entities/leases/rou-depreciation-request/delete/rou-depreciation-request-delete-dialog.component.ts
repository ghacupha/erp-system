import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouDepreciationRequest } from '../rou-depreciation-request.model';
import { RouDepreciationRequestService } from '../service/rou-depreciation-request.service';

@Component({
  templateUrl: './rou-depreciation-request-delete-dialog.component.html',
})
export class RouDepreciationRequestDeleteDialogComponent {
  rouDepreciationRequest?: IRouDepreciationRequest;

  constructor(protected rouDepreciationRequestService: RouDepreciationRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouDepreciationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
