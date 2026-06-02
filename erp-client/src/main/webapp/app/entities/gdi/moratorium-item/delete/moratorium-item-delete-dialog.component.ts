import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoratoriumItem } from '../moratorium-item.model';
import { MoratoriumItemService } from '../service/moratorium-item.service';

@Component({
  templateUrl: './moratorium-item-delete-dialog.component.html',
})
export class MoratoriumItemDeleteDialogComponent {
  moratoriumItem?: IMoratoriumItem;

  constructor(protected moratoriumItemService: MoratoriumItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moratoriumItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
