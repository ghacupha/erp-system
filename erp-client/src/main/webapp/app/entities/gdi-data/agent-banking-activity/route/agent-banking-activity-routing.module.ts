import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgentBankingActivityComponent } from '../list/agent-banking-activity.component';
import { AgentBankingActivityDetailComponent } from '../detail/agent-banking-activity-detail.component';
import { AgentBankingActivityUpdateComponent } from '../update/agent-banking-activity-update.component';
import { AgentBankingActivityRoutingResolveService } from './agent-banking-activity-routing-resolve.service';

const agentBankingActivityRoute: Routes = [
  {
    path: '',
    component: AgentBankingActivityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgentBankingActivityDetailComponent,
    resolve: {
      agentBankingActivity: AgentBankingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgentBankingActivityUpdateComponent,
    resolve: {
      agentBankingActivity: AgentBankingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgentBankingActivityUpdateComponent,
    resolve: {
      agentBankingActivity: AgentBankingActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(agentBankingActivityRoute)],
  exports: [RouterModule],
})
export class AgentBankingActivityRoutingModule {}
