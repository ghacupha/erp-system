import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAgentBankingActivity } from '../agent-banking-activity.model';
import { AgentBankingActivityService } from '../service/agent-banking-activity.service';

@Component({
  templateUrl: './agent-banking-activity-delete-dialog.component.html',
})
export class AgentBankingActivityDeleteDialogComponent {
  agentBankingActivity?: IAgentBankingActivity;

  constructor(protected agentBankingActivityService: AgentBankingActivityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agentBankingActivityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
