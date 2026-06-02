import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbSubmittingInstitutionCategory } from '../crb-submitting-institution-category.model';
import { CrbSubmittingInstitutionCategoryService } from '../service/crb-submitting-institution-category.service';

@Component({
  templateUrl: './crb-submitting-institution-category-delete-dialog.component.html',
})
export class CrbSubmittingInstitutionCategoryDeleteDialogComponent {
  crbSubmittingInstitutionCategory?: ICrbSubmittingInstitutionCategory;

  constructor(
    protected crbSubmittingInstitutionCategoryService: CrbSubmittingInstitutionCategoryService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbSubmittingInstitutionCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
