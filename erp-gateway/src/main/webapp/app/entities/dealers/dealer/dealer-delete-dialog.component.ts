import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDealer } from 'app/shared/model/dealers/dealer.model';
import { DealerService } from './dealer.service';

@Component({
  templateUrl: './dealer-delete-dialog.component.html',
})
export class DealerDeleteDialogComponent {
  dealer?: IDealer;

  constructor(protected dealerService: DealerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dealerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dealerListModification');
      this.activeModal.close();
    });
  }
}
