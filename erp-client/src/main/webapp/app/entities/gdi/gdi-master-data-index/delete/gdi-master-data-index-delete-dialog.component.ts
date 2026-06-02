import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGdiMasterDataIndex } from '../gdi-master-data-index.model';
import { GdiMasterDataIndexService } from '../service/gdi-master-data-index.service';

@Component({
  templateUrl: './gdi-master-data-index-delete-dialog.component.html',
})
export class GdiMasterDataIndexDeleteDialogComponent {
  gdiMasterDataIndex?: IGdiMasterDataIndex;

  constructor(protected gdiMasterDataIndexService: GdiMasterDataIndexService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gdiMasterDataIndexService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
