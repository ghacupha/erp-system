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
  leaseTemplateCreationFromLeaseContractInitiatedFromList,
  leaseTemplatePrefillDataLoaded,
  leaseTemplatePrefillDataLoadFailed,
} from '../actions/lease-template-update-status.actions';
import { LeaseTemplateWorkflowEffects } from './lease-template-workflow.effects';

describe('LeaseTemplateWorkflowEffects', () => {
  let actions$: ReplaySubject<Action>;
  let effects: LeaseTemplateWorkflowEffects;

  const leaseTemplateService = { find: jest.fn() };
  const leaseContractService = { find: jest.fn() };
  const taAmortizationRuleService = { query: jest.fn() };
  const taInterestPaidTransferRuleService = { query: jest.fn() };
  const taLeaseInterestAccrualRuleService = { query: jest.fn() };
  const taLeaseRecognitionRuleService = { query: jest.fn() };
  const taLeaseRepaymentRuleService = { query: jest.fn() };
  const taRecognitionRouRuleService = { query: jest.fn() };
  const rouModelMetadataService = { query: jest.fn() };

  beforeEach(() => {
    actions$ = new ReplaySubject<Action>(1);

    effects = new LeaseTemplateWorkflowEffects(
      actions$ as Actions,
      leaseTemplateService as any,
      leaseContractService as any,
      taAmortizationRuleService as any,
      taInterestPaidTransferRuleService as any,
      taLeaseInterestAccrualRuleService as any,
      taLeaseRecognitionRuleService as any,
      taLeaseRepaymentRuleService as any,
      taRecognitionRouRuleService as any,
      rouModelMetadataService as any
    );
  });

  it('should load prefill data from related sources', done => {
    const leaseContract = {
      id: 12,
      bookingId: 'BOOK-12',
      superintendentServiceOutlet: { id: 1 },
      mainDealer: { id: 2 },
    };

    leaseContractService.find.mockReturnValue(of(new HttpResponse({ body: leaseContract })));
    taAmortizationRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 10 }, credit: { id: 11 } }] }))
    );
    taInterestPaidTransferRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 20 }, credit: { id: 21 } }] }))
    );
    taLeaseInterestAccrualRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 30 }, credit: { id: 31 } }] }))
    );
    taLeaseRecognitionRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 40 }, credit: { id: 41 } }] }))
    );
    taLeaseRepaymentRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 50 }, credit: { id: 51 } }] }))
    );
    taRecognitionRouRuleService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ debit: { id: 60 }, credit: { id: 61 } }] }))
    );
    rouModelMetadataService.query.mockReturnValue(
      of(new HttpResponse({ body: [{ assetCategory: { id: 99 } }] }))
    );

    actions$.next(
      leaseTemplateCreationFromLeaseContractInitiatedFromList({ sourceLeaseContract: leaseContract })
    );

    effects.leaseTemplatePrefillFromLeaseContractEffect$.subscribe(result => {
      expect(result).toEqual(
        leaseTemplatePrefillDataLoaded({
          prefillTemplate: expect.objectContaining({
            templateTitle: 'BOOK-12',
            serviceOutlet: { id: 1 },
            mainDealer: { id: 2 },
            depreciationAccount: { id: 10 },
            accruedDepreciationAccount: { id: 11 },
            interestPaidTransferDebitAccount: { id: 20 },
            interestPaidTransferCreditAccount: { id: 21 },
            interestAccruedDebitAccount: { id: 30 },
            interestAccruedCreditAccount: { id: 31 },
            leaseRecognitionDebitAccount: { id: 40 },
            leaseRecognitionCreditAccount: { id: 41 },
            leaseRepaymentDebitAccount: { id: 50 },
            leaseRepaymentCreditAccount: { id: 51 },
            rouRecognitionDebitAccount: { id: 60 },
            rouRecognitionCreditAccount: { id: 61 },
            assetAccount: { id: 60 },
            assetCategory: { id: 99 },
          }),
          sourceLeaseContract: leaseContract,
        })
      );
      done();
    });
  });

  it('should emit a failure action when the lease contract id is missing', done => {
    actions$.next(
      leaseTemplateCreationFromLeaseContractInitiatedFromList({ sourceLeaseContract: {} })
    );

    effects.leaseTemplatePrefillFromLeaseContractEffect$.subscribe(result => {
      expect(result).toEqual(
        leaseTemplatePrefillDataLoadFailed({ error: 'Lease contract id is missing.' })
      );
      done();
    });
  });
});
