import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoryOfSecurity } from '../category-of-security.model';
import { CategoryOfSecurityService } from '../service/category-of-security.service';

@Component({
  templateUrl: './category-of-security-delete-dialog.component.html',
})
export class CategoryOfSecurityDeleteDialogComponent {
  categoryOfSecurity?: ICategoryOfSecurity;

  constructor(protected categoryOfSecurityService: CategoryOfSecurityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoryOfSecurityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
