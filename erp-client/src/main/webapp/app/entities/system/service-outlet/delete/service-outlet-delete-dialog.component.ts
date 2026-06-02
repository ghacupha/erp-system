import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceOutlet } from '../service-outlet.model';
import { ServiceOutletService } from '../service/service-outlet.service';

@Component({
  templateUrl: './service-outlet-delete-dialog.component.html',
})
export class ServiceOutletDeleteDialogComponent {
  serviceOutlet?: IServiceOutlet;

  constructor(protected serviceOutletService: ServiceOutletService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceOutletService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
