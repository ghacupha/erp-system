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

import { Inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { forkJoin, of } from 'rxjs';
import {
  leaseTemplateCopyWorkflowInitiatedFromList,
  leaseTemplateCreationFromLeaseContractInitiatedFromList,
  leaseTemplateCreationFromLeaseContractInitiatedFromView,
  leaseTemplatePrefillDataLoaded,
  leaseTemplatePrefillDataLoadFailed,
  leaseTemplateUpdateInstanceAcquiredFromBackend,
  leaseTemplateUpdateInstanceAcquisitionFromBackendFailed
} from '../actions/lease-template-update-status.actions';
import { LeaseTemplateService } from '../../erp-leases/lease-template/service/lease-template.service';
import { IFRS16LeaseContractService } from '../../erp-leases/ifrs-16-lease-contract/service/ifrs-16-lease-contract.service';
import { TAAmortizationRuleService } from '../../erp-accounts/ta-amortization-rule/service/ta-amortization-rule.service';
import { TAInterestPaidTransferRuleService } from '../../erp-accounts/ta-interest-paid-transfer-rule/service/ta-interest-paid-transfer-rule.service';
import { TALeaseInterestAccrualRuleService } from '../../erp-accounts/ta-lease-interest-accrual-rule/service/ta-lease-interest-accrual-rule.service';
import { TALeaseRecognitionRuleService } from '../../erp-accounts/ta-lease-recognition-rule/service/ta-lease-recognition-rule.service';
import { TALeaseRepaymentRuleService } from '../../erp-accounts/ta-lease-repayment-rule/service/ta-lease-repayment-rule.service';
import { TARecognitionROURuleService } from '../../erp-accounts/ta-recognition-rou-rule/service/ta-recognition-rou-rule.service';
import { RouModelMetadataService } from '../../erp-leases/rou-model-metadata/service/rou-model-metadata.service';
import { ILeaseTemplate } from '../../erp-leases/lease-template/lease-template.model';

@Injectable()
export class LeaseTemplateWorkflowEffects {

  copiedLeaseTemplateWorkflowEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(leaseTemplateCopyWorkflowInitiatedFromList),
      switchMap(action => {
        if (action.copiedInstance.id) {
          return this.leaseTemplateService.find(action.copiedInstance.id).pipe(
            map(backendResponse => leaseTemplateUpdateInstanceAcquiredFromBackend({
              backendAcquiredInstance: backendResponse.body ?? action.copiedInstance
            })),
            catchError(err => of(leaseTemplateUpdateInstanceAcquisitionFromBackendFailed({ error: err })))
          );
        }
        return of(action);
      })
    )
  );

  leaseTemplatePrefillFromLeaseContractEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(leaseTemplateCreationFromLeaseContractInitiatedFromList, leaseTemplateCreationFromLeaseContractInitiatedFromView),
      switchMap(action => {
        const leaseContractId = action.sourceLeaseContract?.id;

        if (!leaseContractId) {
          return of(leaseTemplatePrefillDataLoadFailed({ error: 'Lease contract id is missing.' }));
        }

        return forkJoin({
          leaseContract: this.leaseContractService.find(leaseContractId),
          amortization: this.taAmortizationRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          interestPaidTransfer: this.taInterestPaidTransferRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          interestAccrued: this.taLeaseInterestAccrualRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          leaseRecognition: this.taLeaseRecognitionRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          leaseRepayment: this.taLeaseRepaymentRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          rouRecognition: this.taRecognitionRouRuleService.query({ 'leaseContractId.equals': leaseContractId, size: 1 }),
          rouMetadata: this.rouModelMetadataService.query({ 'ifrs16LeaseContractId.equals': leaseContractId, size: 1 }),
        }).pipe(
          map(({ leaseContract, amortization, interestPaidTransfer, interestAccrued, leaseRecognition, leaseRepayment, rouRecognition, rouMetadata }) => {
            const contract = leaseContract.body ?? action.sourceLeaseContract;
            const amortizationRule = amortization.body?.[0];
            const interestPaidTransferRule = interestPaidTransfer.body?.[0];
            const interestAccruedRule = interestAccrued.body?.[0];
            const leaseRecognitionRule = leaseRecognition.body?.[0];
            const leaseRepaymentRule = leaseRepayment.body?.[0];
            const rouRecognitionRule = rouRecognition.body?.[0];
            const rouModelMetadata = rouMetadata.body?.[0];

            const prefillTemplate: Partial<ILeaseTemplate> = {
              templateTitle: contract?.bookingId ?? undefined,
              serviceOutlet: contract?.superintendentServiceOutlet ?? undefined,
              mainDealer: contract?.mainDealer ?? undefined,
              depreciationAccount: amortizationRule?.debit,
              accruedDepreciationAccount: amortizationRule?.credit,
              interestPaidTransferDebitAccount: interestPaidTransferRule?.debit,
              interestPaidTransferCreditAccount: interestPaidTransferRule?.credit,
              interestAccruedDebitAccount: interestAccruedRule?.debit,
              interestAccruedCreditAccount: interestAccruedRule?.credit,
              leaseRecognitionDebitAccount: leaseRecognitionRule?.debit,
              leaseRecognitionCreditAccount: leaseRecognitionRule?.credit,
              leaseRepaymentDebitAccount: leaseRepaymentRule?.debit,
              leaseRepaymentCreditAccount: leaseRepaymentRule?.credit,
              rouRecognitionDebitAccount: rouRecognitionRule?.debit,
              rouRecognitionCreditAccount: rouRecognitionRule?.credit,
              assetAccount: rouRecognitionRule?.debit,
              assetCategory: rouModelMetadata?.assetCategory,
            };

            return leaseTemplatePrefillDataLoaded({
              prefillTemplate,
              sourceLeaseContract: contract ?? action.sourceLeaseContract,
            });
          }),
          catchError(error => of(leaseTemplatePrefillDataLoadFailed({ error })))
        );
      })
    )
  );

  constructor(
    protected actions$: Actions,
    @Inject(LeaseTemplateService) protected leaseTemplateService: LeaseTemplateService,
    protected leaseContractService: IFRS16LeaseContractService,
    protected taAmortizationRuleService: TAAmortizationRuleService,
    protected taInterestPaidTransferRuleService: TAInterestPaidTransferRuleService,
    protected taLeaseInterestAccrualRuleService: TALeaseInterestAccrualRuleService,
    protected taLeaseRecognitionRuleService: TALeaseRecognitionRuleService,
    protected taLeaseRepaymentRuleService: TALeaseRepaymentRuleService,
    protected taRecognitionRouRuleService: TARecognitionROURuleService,
    protected rouModelMetadataService: RouModelMetadataService
  ) {}
}
