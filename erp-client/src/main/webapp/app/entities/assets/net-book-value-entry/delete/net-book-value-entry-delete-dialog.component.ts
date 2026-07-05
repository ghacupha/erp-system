import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INetBookValueEntry } from '../net-book-value-entry.model';
import { NetBookValueEntryService } from '../service/net-book-value-entry.service';

@Component({
  templateUrl: './net-book-value-entry-delete-dialog.component.html',
})
export class NetBookValueEntryDeleteDialogComponent {
  netBookValueEntry?: INetBookValueEntry;

  constructor(protected netBookValueEntryService: NetBookValueEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.netBookValueEntryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
