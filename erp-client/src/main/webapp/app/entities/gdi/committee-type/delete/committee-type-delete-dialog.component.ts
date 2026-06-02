import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommitteeType } from '../committee-type.model';
import { CommitteeTypeService } from '../service/committee-type.service';

@Component({
  templateUrl: './committee-type-delete-dialog.component.html',
})
export class CommitteeTypeDeleteDialogComponent {
  committeeType?: ICommitteeType;

  constructor(protected committeeTypeService: CommitteeTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.committeeTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
