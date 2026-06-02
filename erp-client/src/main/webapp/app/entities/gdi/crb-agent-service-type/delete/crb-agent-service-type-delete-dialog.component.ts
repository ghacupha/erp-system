import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbAgentServiceType } from '../crb-agent-service-type.model';
import { CrbAgentServiceTypeService } from '../service/crb-agent-service-type.service';

@Component({
  templateUrl: './crb-agent-service-type-delete-dialog.component.html',
})
export class CrbAgentServiceTypeDeleteDialogComponent {
  crbAgentServiceType?: ICrbAgentServiceType;

  constructor(protected crbAgentServiceTypeService: CrbAgentServiceTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbAgentServiceTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
