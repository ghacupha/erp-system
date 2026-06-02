import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaseTemplate } from '../lease-template.model';
import { LeaseTemplateService } from '../service/lease-template.service';

@Component({
  templateUrl: './lease-template-delete-dialog.component.html',
})
export class LeaseTemplateDeleteDialogComponent {
  leaseTemplate?: ILeaseTemplate;

  constructor(protected leaseTemplateService: LeaseTemplateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseTemplateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
