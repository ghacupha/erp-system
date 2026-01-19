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
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import { IFRS16LeaseContractService } from '../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { LeaseTemplateService } from '../../erp-leases/lease-template/service/lease-template.service';
import {
  leasePostingRuleLeaseContractSelected,
  leasePostingRuleSuggestionsFailed,
  leasePostingRuleSuggestionsUpdated,
} from '../actions/lease-posting-rule-config.actions';
import { ILeaseTemplate } from '../../erp-leases/lease-template/lease-template.model';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';

@Injectable()
export class LeasePostingRuleConfigEffects {
  loadLeaseTemplateSuggestions$ = createEffect(() =>
    this.actions$.pipe(
      ofType(leasePostingRuleLeaseContractSelected),
      switchMap(({ leaseContract, eventType }) =>
        this.leaseContractService.find(leaseContract.id!).pipe(
          switchMap((response: HttpResponse<IIFRS16LeaseContract>) => {
            const template = response.body?.leaseTemplate;
            const templateId = template?.id;
            if (!templateId) {
              return of(
                leasePostingRuleSuggestionsUpdated({
                  suggestions: {
                    debitAccount: null,
                    creditAccount: null,
                    debitAccountType: null,
                    creditAccountType: null,
                  },
                })
              );
            }
            return this.leaseTemplateService.find(templateId).pipe(
              map((templateResponse: HttpResponse<ILeaseTemplate>) => {
                const leaseTemplate = templateResponse.body ?? template;
                const { debitAccount, creditAccount } = this.mapAccountsForEventType(leaseTemplate, eventType);
                return leasePostingRuleSuggestionsUpdated({
                  suggestions: {
                    debitAccount,
                    creditAccount,
                    debitAccountType: debitAccount?.accountCategory ?? null,
                    creditAccountType: creditAccount?.accountCategory ?? null,
                  },
                });
              })
            );
          }),
          catchError(error =>
            of(
              leasePostingRuleSuggestionsFailed({
                error: error?.message ?? 'Unable to load lease posting rule suggestions',
              })
            )
          )
        )
      )
    )
  );

  constructor(
    protected actions$: Actions,
    protected leaseContractService: IFRS16LeaseContractService,
    protected leaseTemplateService: LeaseTemplateService
  ) {}

  protected mapAccountsForEventType(
    leaseTemplate: ILeaseTemplate | undefined,
    eventType?: string | null
  ): { debitAccount: ITransactionAccount | null; creditAccount: ITransactionAccount | null } {
    if (!leaseTemplate || !eventType) {
      return { debitAccount: null, creditAccount: null };
    }

    switch (eventType) {
      case 'LEASE_LIABILITY_RECOGNITION':
        return {
          debitAccount: leaseTemplate.leaseRecognitionDebitAccount ?? null,
          creditAccount: leaseTemplate.leaseRecognitionCreditAccount ?? null,
        };
      case 'LEASE_REPAYMENT':
        return {
          debitAccount: leaseTemplate.leaseRepaymentDebitAccount ?? null,
          creditAccount: leaseTemplate.leaseRepaymentCreditAccount ?? null,
        };
      case 'LEASE_INTEREST_ACCRUAL':
        return {
          debitAccount: leaseTemplate.interestAccruedDebitAccount ?? null,
          creditAccount: leaseTemplate.interestAccruedCreditAccount ?? null,
        };
      case 'LEASE_INTEREST_PAID_TRANSFER':
        return {
          debitAccount: leaseTemplate.interestPaidTransferDebitAccount ?? null,
          creditAccount: leaseTemplate.interestPaidTransferCreditAccount ?? null,
        };
      case 'LEASE_ROU_RECOGNITION':
        return {
          debitAccount: leaseTemplate.rouRecognitionDebitAccount ?? null,
          creditAccount: leaseTemplate.rouRecognitionCreditAccount ?? null,
        };
      case 'LEASE_ROU_AMORTIZATION':
        return {
          debitAccount: leaseTemplate.depreciationAccount ?? null,
          creditAccount: leaseTemplate.accruedDepreciationAccount ?? null,
        };
      default:
        return { debitAccount: null, creditAccount: null };
    }
  }
}
