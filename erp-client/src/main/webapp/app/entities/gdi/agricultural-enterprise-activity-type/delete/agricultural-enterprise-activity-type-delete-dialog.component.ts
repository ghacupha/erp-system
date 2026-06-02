import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgriculturalEnterpriseActivityType } from '../agricultural-enterprise-activity-type.model';
import { AgriculturalEnterpriseActivityTypeService } from '../service/agricultural-enterprise-activity-type.service';

@Component({
  templateUrl: './agricultural-enterprise-activity-type-delete-dialog.component.html',
})
export class AgriculturalEnterpriseActivityTypeDeleteDialogComponent {
  agriculturalEnterpriseActivityType?: IAgriculturalEnterpriseActivityType;

  constructor(
    protected agriculturalEnterpriseActivityTypeService: AgriculturalEnterpriseActivityTypeService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agriculturalEnterpriseActivityTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
