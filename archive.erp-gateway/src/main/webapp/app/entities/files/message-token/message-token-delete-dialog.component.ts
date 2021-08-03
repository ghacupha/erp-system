import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMessageToken } from 'app/shared/model/files/message-token.model';
import { MessageTokenService } from './message-token.service';

@Component({
  templateUrl: './message-token-delete-dialog.component.html',
})
export class MessageTokenDeleteDialogComponent {
  messageToken?: IMessageToken;

  constructor(
    protected messageTokenService: MessageTokenService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.messageTokenService.delete(id).subscribe(() => {
      this.eventManager.broadcast('messageTokenListModification');
      this.activeModal.close();
    });
  }
}
