import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AgentBankingActivityComponent } from './list/agent-banking-activity.component';
import { AgentBankingActivityDetailComponent } from './detail/agent-banking-activity-detail.component';
import { AgentBankingActivityUpdateComponent } from './update/agent-banking-activity-update.component';
import { AgentBankingActivityDeleteDialogComponent } from './delete/agent-banking-activity-delete-dialog.component';
import { AgentBankingActivityRoutingModule } from './route/agent-banking-activity-routing.module';

@NgModule({
  imports: [SharedModule, AgentBankingActivityRoutingModule],
  declarations: [
    AgentBankingActivityComponent,
    AgentBankingActivityDetailComponent,
    AgentBankingActivityUpdateComponent,
    AgentBankingActivityDeleteDialogComponent,
  ],
  entryComponents: [AgentBankingActivityDeleteDialogComponent],
})
export class AgentBankingActivityModule {}
