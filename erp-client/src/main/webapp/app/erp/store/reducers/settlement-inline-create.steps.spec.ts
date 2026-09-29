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

import { defineFeature, loadFeature } from 'jest-cucumber';
import {
  initialSettlementInlineCreateState,
  settlementInlineCreateReducer,
  SettlementInlineCreateState,
} from './settlement-inline-create.reducer';
import {
  settlementInlineCreateCancelled,
  settlementInlineCreateCompleted,
  settlementInlineCreateConsumed,
  settlementInlineCreateStarted,
} from '../actions/settlement-inline-create.actions';
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';
import { IPrepaymentAccount } from '../../erp-prepayments/prepayment-account/prepayment-account.model';

const feature = loadFeature('./settlement-inline-create.feature');

defineFeature(feature, test => {
  let state: SettlementInlineCreateState;
  const draftPrepaymentAccount: IPrepaymentAccount = { catalogueNumber: '20732' };
  const savedSettlement: ISettlement = { id: 999, paymentNumber: 'DC317' };

  test('Starting a Settlement inline-create captures the parent draft', ({ given, when, then, and }) => {
    given('a Prepayment Account form has draft values entered', () => {
      state = initialSettlementInlineCreateState;
    });

    when('it starts an inline Settlement create for the "prepaymentTransaction" field', () => {
      state = settlementInlineCreateReducer(
        state,
        settlementInlineCreateStarted({
          targetField: 'prepaymentTransaction',
          parentRoute: '/prepayment-account/456/edit',
          parentFormSnapshot: draftPrepaymentAccount,
        })
      );
    });

    then('the workflow is active', () => {
      expect(state.active).toBe(true);
    });

    and("the parent's draft is stored for later restoration", () => {
      expect(state.parentFormSnapshot).toEqual(draftPrepaymentAccount);
      expect(state.targetField).toBe('prepaymentTransaction');
      expect(state.parentRoute).toBe('/prepayment-account/456/edit');
    });

    and('no Settlement has been created yet', () => {
      expect(state.createdSettlement).toBeNull();
    });
  });

  test('Completing the Settlement inline-create records what was saved', ({ given, when, then, and }) => {
    given('a Settlement inline-create is active', () => {
      state = settlementInlineCreateReducer(
        initialSettlementInlineCreateState,
        settlementInlineCreateStarted({
          targetField: 'prepaymentTransaction',
          parentRoute: '/prepayment-account/456/edit',
          parentFormSnapshot: draftPrepaymentAccount,
        })
      );
    });

    when('the Settlement form reports a saved Settlement', () => {
      state = settlementInlineCreateReducer(state, settlementInlineCreateCompleted({ createdSettlement: savedSettlement }));
    });

    then('the created Settlement is available to the parent form', () => {
      expect(state.createdSettlement).toEqual(savedSettlement);
    });

    and('the workflow is still active until explicitly consumed', () => {
      expect(state.active).toBe(true);
    });
  });

  test('Consuming the completion resets the workflow', ({ given, when, then, and }) => {
    given('a Settlement inline-create has a completed Settlement', () => {
      state = settlementInlineCreateReducer(
        initialSettlementInlineCreateState,
        settlementInlineCreateStarted({
          targetField: 'prepaymentTransaction',
          parentRoute: '/prepayment-account/456/edit',
          parentFormSnapshot: draftPrepaymentAccount,
        })
      );
      state = settlementInlineCreateReducer(state, settlementInlineCreateCompleted({ createdSettlement: savedSettlement }));
    });

    when('the parent form consumes the completion', () => {
      state = settlementInlineCreateReducer(state, settlementInlineCreateConsumed());
    });

    then('the workflow is no longer active', () => {
      expect(state.active).toBe(false);
    });

    and('no draft or created Settlement remains', () => {
      expect(state.parentFormSnapshot).toBeNull();
      expect(state.createdSettlement).toBeNull();
    });
  });

  test('Cancelling an inline-create resets the workflow', ({ given, when, then }) => {
    given('a Settlement inline-create is active', () => {
      state = settlementInlineCreateReducer(
        initialSettlementInlineCreateState,
        settlementInlineCreateStarted({
          targetField: 'prepaymentTransaction',
          parentRoute: '/prepayment-account/456/edit',
          parentFormSnapshot: draftPrepaymentAccount,
        })
      );
    });

    when('it is cancelled', () => {
      state = settlementInlineCreateReducer(state, settlementInlineCreateCancelled());
    });

    then('the workflow is no longer active', () => {
      expect(state.active).toBe(false);
    });
  });
});
