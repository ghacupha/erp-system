import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgentBankingActivity } from '../agent-banking-activity.model';

@Component({
  selector: 'jhi-agent-banking-activity-detail',
  templateUrl: './agent-banking-activity-detail.component.html',
})
export class AgentBankingActivityDetailComponent implements OnInit {
  agentBankingActivity: IAgentBankingActivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentBankingActivity }) => {
      this.agentBankingActivity = agentBankingActivity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
