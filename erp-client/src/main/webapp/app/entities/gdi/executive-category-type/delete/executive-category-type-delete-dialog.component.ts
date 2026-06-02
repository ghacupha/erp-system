import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExecutiveCategoryType } from '../executive-category-type.model';
import { ExecutiveCategoryTypeService } from '../service/executive-category-type.service';

@Component({
  templateUrl: './executive-category-type-delete-dialog.component.html',
})
export class ExecutiveCategoryTypeDeleteDialogComponent {
  executiveCategoryType?: IExecutiveCategoryType;

  constructor(protected executiveCategoryTypeService: ExecutiveCategoryTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.executiveCategoryTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
