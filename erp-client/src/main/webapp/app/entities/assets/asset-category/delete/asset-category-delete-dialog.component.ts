import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssetCategory } from '../asset-category.model';
import { AssetCategoryService } from '../service/asset-category.service';

@Component({
  templateUrl: './asset-category-delete-dialog.component.html',
})
export class AssetCategoryDeleteDialogComponent {
  assetCategory?: IAssetCategory;

  constructor(protected assetCategoryService: AssetCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assetCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
