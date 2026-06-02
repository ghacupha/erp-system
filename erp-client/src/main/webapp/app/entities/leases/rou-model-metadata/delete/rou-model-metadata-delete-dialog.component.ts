import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRouModelMetadata } from '../rou-model-metadata.model';
import { RouModelMetadataService } from '../service/rou-model-metadata.service';

@Component({
  templateUrl: './rou-model-metadata-delete-dialog.component.html',
})
export class RouModelMetadataDeleteDialogComponent {
  rouModelMetadata?: IRouModelMetadata;

  constructor(protected rouModelMetadataService: RouModelMetadataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rouModelMetadataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
