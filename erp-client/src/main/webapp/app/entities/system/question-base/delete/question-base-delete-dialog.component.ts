import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionBase } from '../question-base.model';
import { QuestionBaseService } from '../service/question-base.service';

@Component({
  templateUrl: './question-base-delete-dialog.component.html',
})
export class QuestionBaseDeleteDialogComponent {
  questionBase?: IQuestionBase;

  constructor(protected questionBaseService: QuestionBaseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionBaseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
