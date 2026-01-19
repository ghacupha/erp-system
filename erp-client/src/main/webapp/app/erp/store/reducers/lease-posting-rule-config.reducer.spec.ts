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

import {
  initialLeasePostingRuleConfigState,
  leasePostingRuleConfigReducer,
} from './lease-posting-rule-config.reducer';
import { leasePostingRuleFormUpdated, leasePostingRuleResetDraft, leasePostingRuleSuggestionsUpdated } from '../actions/lease-posting-rule-config.actions';

const sampleRule = {
  name: 'Lease Recognition Rule',
  module: 'LEASE',
  eventType: 'LEASE_LIABILITY_RECOGNITION',
};

describe('LeasePostingRuleConfigReducer', () => {
  it('should update the draft when form data changes', () => {
    const result = leasePostingRuleConfigReducer(initialLeasePostingRuleConfigState, leasePostingRuleFormUpdated({ draft: sampleRule }));
    expect(result.draft).toEqual(sampleRule);
  });

  it('should apply suggestions for accounts and account categories', () => {
    const result = leasePostingRuleConfigReducer(
      initialLeasePostingRuleConfigState,
      leasePostingRuleSuggestionsUpdated({
        suggestions: {
          debitAccount: { id: 10 },
          creditAccount: { id: 20 },
          debitAccountType: { id: 30 },
          creditAccountType: { id: 40 },
        },
      })
    );

    expect(result.suggestedDebitAccount?.id).toBe(10);
    expect(result.suggestedCreditAccount?.id).toBe(20);
    expect(result.suggestedDebitAccountType?.id).toBe(30);
    expect(result.suggestedCreditAccountType?.id).toBe(40);
  });

  it('should reset the draft state', () => {
    const mutatedState = leasePostingRuleConfigReducer(initialLeasePostingRuleConfigState, leasePostingRuleFormUpdated({ draft: sampleRule }));
    const result = leasePostingRuleConfigReducer(mutatedState, leasePostingRuleResetDraft());
    expect(result).toEqual(initialLeasePostingRuleConfigState);
  });
});
