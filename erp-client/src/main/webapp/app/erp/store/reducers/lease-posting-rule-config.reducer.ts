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

import { createReducer, on } from '@ngrx/store';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { ITransactionAccountPostingRule } from '../../erp-accounts/transaction-account-posting-rule/transaction-account-posting-rule.model';
import {
  leasePostingRuleFormUpdated,
  leasePostingRuleLeaseContractSelected,
  leasePostingRuleResetDraft,
  leasePostingRuleSuggestionsUpdated,
} from '../actions/lease-posting-rule-config.actions';

export const leasePostingRuleConfigFeatureKey = 'leasePostingRuleConfig';

export interface LeasePostingRuleConfigState {
  draft: ITransactionAccountPostingRule;
  selectedLeaseContract?: IIFRS16LeaseContract | null;
  suggestedDebitAccount?: ITransactionAccount | null;
  suggestedCreditAccount?: ITransactionAccount | null;
  suggestedDebitAccountType?: ITransactionAccountCategory | null;
  suggestedCreditAccountType?: ITransactionAccountCategory | null;
}

export const initialLeasePostingRuleConfigState: LeasePostingRuleConfigState = {
  draft: {},
  selectedLeaseContract: null,
  suggestedDebitAccount: null,
  suggestedCreditAccount: null,
  suggestedDebitAccountType: null,
  suggestedCreditAccountType: null,
};

export const leasePostingRuleConfigReducer = createReducer(
  initialLeasePostingRuleConfigState,
  on(leasePostingRuleFormUpdated, (state, { draft }) => ({
    ...state,
    draft,
  })),
  on(leasePostingRuleLeaseContractSelected, (state, { leaseContract }) => ({
    ...state,
    selectedLeaseContract: leaseContract,
  })),
  on(leasePostingRuleSuggestionsUpdated, (state, { suggestions }) => ({
    ...state,
    suggestedDebitAccount: suggestions.debitAccount ?? null,
    suggestedCreditAccount: suggestions.creditAccount ?? null,
    suggestedDebitAccountType: suggestions.debitAccountType ?? null,
    suggestedCreditAccountType: suggestions.creditAccountType ?? null,
  })),
  on(leasePostingRuleResetDraft, () => ({ ...initialLeasePostingRuleConfigState }))
);
