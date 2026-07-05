import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationEntry } from '../depreciation-entry.model';
import { DepreciationEntryService } from '../service/depreciation-entry.service';

@Component({
  templateUrl: './depreciation-entry-delete-dialog.component.html',
})
export class DepreciationEntryDeleteDialogComponent {
  depreciationEntry?: IDepreciationEntry;

  constructor(protected depreciationEntryService: DepreciationEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationEntryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
