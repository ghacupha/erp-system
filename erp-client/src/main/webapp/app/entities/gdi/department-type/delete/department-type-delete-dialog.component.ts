import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepartmentType } from '../department-type.model';
import { DepartmentTypeService } from '../service/department-type.service';

@Component({
  templateUrl: './department-type-delete-dialog.component.html',
})
export class DepartmentTypeDeleteDialogComponent {
  departmentType?: IDepartmentType;

  constructor(protected departmentTypeService: DepartmentTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departmentTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
