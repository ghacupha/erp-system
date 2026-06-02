import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStringQuestionBase } from '../string-question-base.model';
import { StringQuestionBaseService } from '../service/string-question-base.service';

@Component({
  templateUrl: './string-question-base-delete-dialog.component.html',
})
export class StringQuestionBaseDeleteDialogComponent {
  stringQuestionBase?: IStringQuestionBase;

  constructor(protected stringQuestionBaseService: StringQuestionBaseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stringQuestionBaseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
