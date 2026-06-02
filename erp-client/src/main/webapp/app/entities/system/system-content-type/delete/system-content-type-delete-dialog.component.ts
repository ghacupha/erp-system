import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemContentType } from '../system-content-type.model';
import { SystemContentTypeService } from '../service/system-content-type.service';

@Component({
  templateUrl: './system-content-type-delete-dialog.component.html',
})
export class SystemContentTypeDeleteDialogComponent {
  systemContentType?: ISystemContentType;

  constructor(protected systemContentTypeService: SystemContentTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemContentTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
