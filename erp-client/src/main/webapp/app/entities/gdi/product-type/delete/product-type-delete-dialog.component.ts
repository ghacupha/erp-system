import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

@Component({
  templateUrl: './product-type-delete-dialog.component.html',
})
export class ProductTypeDeleteDialogComponent {
  productType?: IProductType;

  constructor(protected productTypeService: ProductTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
