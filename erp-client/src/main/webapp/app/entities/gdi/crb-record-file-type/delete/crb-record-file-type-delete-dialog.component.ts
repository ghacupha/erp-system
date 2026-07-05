import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrbRecordFileType } from '../crb-record-file-type.model';
import { CrbRecordFileTypeService } from '../service/crb-record-file-type.service';

@Component({
  templateUrl: './crb-record-file-type-delete-dialog.component.html',
})
export class CrbRecordFileTypeDeleteDialogComponent {
  crbRecordFileType?: ICrbRecordFileType;

  constructor(protected crbRecordFileTypeService: CrbRecordFileTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crbRecordFileTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
