import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationMethod } from '../depreciation-method.model';
import { DepreciationMethodService } from '../service/depreciation-method.service';

@Component({
  templateUrl: './depreciation-method-delete-dialog.component.html',
})
export class DepreciationMethodDeleteDialogComponent {
  depreciationMethod?: IDepreciationMethod;

  constructor(protected depreciationMethodService: DepreciationMethodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationMethodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
