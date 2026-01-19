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

import { HttpResponse } from '@angular/common/http';
import { Actions } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { ReplaySubject, of } from 'rxjs';
import {
  leasePostingRuleLeaseContractSelected,
  leasePostingRuleSuggestionsUpdated,
} from '../actions/lease-posting-rule-config.actions';
import { LeasePostingRuleConfigEffects } from './lease-posting-rule-config.effects';

describe('LeasePostingRuleConfigEffects', () => {
  let actions$: ReplaySubject<Action>;
  let effects: LeasePostingRuleConfigEffects;

  const leaseContractService = { find: jest.fn() };
  const leaseTemplateService = { find: jest.fn() };

  beforeEach(() => {
    actions$ = new ReplaySubject<Action>(1);

    effects = new LeasePostingRuleConfigEffects(
      actions$ as Actions,
      leaseContractService as any,
      leaseTemplateService as any
    );
  });

  it('should map lease repayment defaults from the lease template', done => {
    leaseContractService.find.mockReturnValue(
      of(new HttpResponse({ body: { id: 10, leaseTemplate: { id: 99 } } }))
    );
    leaseTemplateService.find.mockReturnValue(
      of(
        new HttpResponse({
          body: {
            leaseRepaymentDebitAccount: { id: 1, accountCategory: { id: 11 } },
            leaseRepaymentCreditAccount: { id: 2, accountCategory: { id: 22 } },
          },
        })
      )
    );

    actions$.next(
      leasePostingRuleLeaseContractSelected({
        leaseContract: { id: 10 },
        eventType: 'LEASE_REPAYMENT',
      })
    );

    effects.loadLeaseTemplateSuggestions$.subscribe(result => {
      expect(result).toEqual(
        leasePostingRuleSuggestionsUpdated({
          suggestions: {
            debitAccount: { id: 1, accountCategory: { id: 11 } },
            creditAccount: { id: 2, accountCategory: { id: 22 } },
            debitAccountType: { id: 11 },
            creditAccountType: { id: 22 },
          },
        })
      );
      done();
    });
  });

  it('should clear suggestions when event type is missing', done => {
    leaseContractService.find.mockReturnValue(
      of(new HttpResponse({ body: { id: 10, leaseTemplate: { id: 99 } } }))
    );
    leaseTemplateService.find.mockReturnValue(
      of(
        new HttpResponse({
          body: {
            leaseRecognitionDebitAccount: { id: 3 },
            leaseRecognitionCreditAccount: { id: 4 },
          },
        })
      )
    );

    actions$.next(
      leasePostingRuleLeaseContractSelected({
        leaseContract: { id: 10 },
        eventType: null,
      })
    );

    effects.loadLeaseTemplateSuggestions$.subscribe(result => {
      expect(result).toEqual(
        leasePostingRuleSuggestionsUpdated({
          suggestions: {
            debitAccount: null,
            creditAccount: null,
            debitAccountType: null,
            creditAccountType: null,
          },
        })
      );
      done();
    });
  });
});
