import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInsiderCategoryTypes } from '../insider-category-types.model';
import { InsiderCategoryTypesService } from '../service/insider-category-types.service';

@Component({
  templateUrl: './insider-category-types-delete-dialog.component.html',
})
export class InsiderCategoryTypesDeleteDialogComponent {
  insiderCategoryTypes?: IInsiderCategoryTypes;

  constructor(protected insiderCategoryTypesService: InsiderCategoryTypesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insiderCategoryTypesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
