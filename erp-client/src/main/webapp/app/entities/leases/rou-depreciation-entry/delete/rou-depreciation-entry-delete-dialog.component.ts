import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouDepreciationEntry } from '../rou-depreciation-entry.model';
import { RouDepreciationEntryService } from '../service/rou-depreciation-entry.service';

@Component({
  templateUrl: './rou-depreciation-entry-delete-dialog.component.html',
})
export class RouDepreciationEntryDeleteDialogComponent {
  rouDepreciationEntry?: IRouDepreciationEntry;

  constructor(protected rouDepreciationEntryService: RouDepreciationEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouDepreciationEntryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
