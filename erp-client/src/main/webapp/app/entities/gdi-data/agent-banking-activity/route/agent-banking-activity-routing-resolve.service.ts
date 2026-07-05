import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgentBankingActivity, AgentBankingActivity } from '../agent-banking-activity.model';
import { AgentBankingActivityService } from '../service/agent-banking-activity.service';

@Injectable({ providedIn: 'root' })
export class AgentBankingActivityRoutingResolveService implements Resolve<IAgentBankingActivity> {
  constructor(protected service: AgentBankingActivityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgentBankingActivity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((agentBankingActivity: HttpResponse<AgentBankingActivity>) => {
          if (agentBankingActivity.body) {
            return of(agentBankingActivity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AgentBankingActivity());
  }
}
