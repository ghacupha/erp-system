import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContractMetadata } from '../contract-metadata.model';
import { ContractMetadataService } from '../service/contract-metadata.service';

@Component({
  templateUrl: './contract-metadata-delete-dialog.component.html',
})
export class ContractMetadataDeleteDialogComponent {
  contractMetadata?: IContractMetadata;

  constructor(protected contractMetadataService: ContractMetadataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractMetadataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
