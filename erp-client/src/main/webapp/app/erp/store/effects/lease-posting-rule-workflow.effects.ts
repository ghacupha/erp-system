///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { TransactionAccountPostingRuleService } from '../../erp-accounts/transaction-account-posting-rule/service/transaction-account-posting-rule.service';
import { IFRS16LeaseContractService } from '../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import {
  leasePostingRuleCopyWorkflowInitiatedEnRoute,
  leasePostingRuleCopyWorkflowInitiatedFromList,
  leasePostingRuleEditWorkflowInitiatedEnRoute,
  leasePostingRuleEditWorkflowInitiatedFromList,
  leasePostingRuleUpdateInstanceAcquiredFromBackend,
  leasePostingRuleUpdateInstanceAcquisitionFromBackendFailed,
} from '../actions/lease-posting-rule-workflow-status.actions';
import { ITransactionAccountPostingRule } from '../../erp-accounts/transaction-account-posting-rule/transaction-account-posting-rule.model';

@Injectable()
export class LeasePostingRuleWorkflowEffects {
  loadPostingRuleForWorkflow$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        leasePostingRuleCopyWorkflowInitiatedFromList,
        leasePostingRuleCopyWorkflowInitiatedEnRoute,
        leasePostingRuleEditWorkflowInitiatedFromList,
        leasePostingRuleEditWorkflowInitiatedEnRoute
      ),
      switchMap(action => {
        const candidate = 'copiedInstance' in action ? action.copiedInstance : action.editedInstance;
        const ruleId = candidate?.id;
        if (!ruleId) {
          return of(leasePostingRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'Posting rule id is missing.' }));
        }
        return this.postingRuleService.find(ruleId).pipe(
          switchMap(response => {
            const postingRule = response.body ?? candidate;
            const leaseContractId = this.extractLeaseContractId(postingRule);
            if (!leaseContractId) {
              return of(leasePostingRuleUpdateInstanceAcquiredFromBackend({ backendAcquiredInstance: postingRule }));
            }
            return forkJoin({
              postingRule: of(postingRule),
              leaseContract: this.leaseContractService.find(leaseContractId),
            }).pipe(
              map(({ postingRule: resolvedRule, leaseContract }) =>
                leasePostingRuleUpdateInstanceAcquiredFromBackend({
                  backendAcquiredInstance: resolvedRule,
                  sourceLeaseContract: leaseContract.body ?? null,
                })
              ),
              catchError(() => of(leasePostingRuleUpdateInstanceAcquiredFromBackend({ backendAcquiredInstance: postingRule })))
            );
          }),
          catchError(error => of(leasePostingRuleUpdateInstanceAcquisitionFromBackendFailed({ error })))
        );
      })
    )
  );

  constructor(
    protected actions$: Actions,
    protected postingRuleService: TransactionAccountPostingRuleService,
    protected leaseContractService: IFRS16LeaseContractService
  ) {}

  protected extractLeaseContractId(rule?: ITransactionAccountPostingRule | null): number | null {
    if (!rule?.postingRuleConditions) {
      return null;
    }
    const leaseCondition = rule.postingRuleConditions.find(condition => condition?.conditionKey === 'leaseContractId');
    const leaseContractId = leaseCondition?.conditionValue ? Number(leaseCondition.conditionValue) : null;
    return leaseContractId && !Number.isNaN(leaseContractId) ? leaseContractId : null;
  }
}
