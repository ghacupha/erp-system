import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUniversallyUniqueMapping } from '../universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from '../service/universally-unique-mapping.service';

@Component({
  templateUrl: './universally-unique-mapping-delete-dialog.component.html',
})
export class UniversallyUniqueMappingDeleteDialogComponent {
  universallyUniqueMapping?: IUniversallyUniqueMapping;

  constructor(protected universallyUniqueMappingService: UniversallyUniqueMappingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.universallyUniqueMappingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
