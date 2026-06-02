import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMerchantType } from '../merchant-type.model';
import { MerchantTypeService } from '../service/merchant-type.service';

@Component({
  templateUrl: './merchant-type-delete-dialog.component.html',
})
export class MerchantTypeDeleteDialogComponent {
  merchantType?: IMerchantType;

  constructor(protected merchantTypeService: MerchantTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.merchantTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
