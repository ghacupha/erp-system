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

import { createAction, props } from '@ngrx/store';
import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { ITransactionAccountPostingRule } from '../../erp-accounts/transaction-account-posting-rule/transaction-account-posting-rule.model';

export const leasePostingRuleFormUpdated = createAction(
  '[LeasePostingRule Form] Lease posting rule draft updated',
  props<{ draft: ITransactionAccountPostingRule }>()
);

export const leasePostingRuleLeaseContractSelected = createAction(
  '[LeasePostingRule Form] Lease contract selected',
  props<{ leaseContract: IIFRS16LeaseContract; eventType?: string | null }>()
);

export const leasePostingRuleSuggestionsUpdated = createAction(
  '[LeasePostingRule Effects] Lease posting rule suggestions updated',
  props<{
    suggestions: {
      debitAccount?: ITransactionAccount | null;
      creditAccount?: ITransactionAccount | null;
      debitAccountType?: ITransactionAccountCategory | null;
      creditAccountType?: ITransactionAccountCategory | null;
    };
  }>()
);

export const leasePostingRuleSuggestionsFailed = createAction(
  '[LeasePostingRule Effects] Lease posting rule suggestions failed',
  props<{ error: string }>()
);

export const leasePostingRuleResetDraft = createAction('[LeasePostingRule Form] Lease posting rule draft reset');
