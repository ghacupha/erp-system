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

import {NgModule} from "@angular/core";
import {EffectsModule} from "@ngrx/effects";
import {StoreModule} from "@ngrx/store";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {DealerPaymentsEffects} from "./effects/dealer-payments.effects";
import {DealerInvoiceWorkflowEffects} from "./effects/dealer-invoice-workflow.effects";
import { SettlementUpdatesEffects } from './effects/settlement-updates.effects';
import { PrepaymentAccountWorkflowEffects } from './effects/prepayment-account-workflow.effects';

import * as fromDealerInvoiceWorkflows from "./reducers/dealer-invoice-workflows-status.reducer";
import * as fromDealerWorkflows from "./reducers/dealer-workflows-status.reducer";
import * as fromPaymentUpdates from "./reducers/update-menu-status.reducer";
import * as fromSettlementUpdates from "./reducers/settlement-update-menu-status.reducer";
import * as fromAssetRegistrationUpdates from "./reducers/asset-registration-workflow-status.reducer";
import * as fromAssetAccessoryUpdates from "./reducers/asset-accessory-workflow-status.reducer";
import * as fromWIPRegistrationUpdates from "./reducers/wip-registration-workflow-status.reducer";
import * as fromPrepaymentAccountUpdates from "./reducers/prepayment-account-workflow-status.reducer";
import * as fromPrepaymentMarshallingUpdates from "./reducers/prepayment-marshalling-workflow-status.reducer";
import * as fromPaymentInvoiceUpdates from "./reducers/payment-invoice-workflow-status.reducer";
import * as fromReportNavigationProfile from "./reducers/report-navigation-profile-status.reducer";
import { Ifrs16LeaseModelWorkflowEffects } from './effects/ifrs16-lease-model-workflow.effects';
import { RouModelMetadataWorkflowEffects } from './effects/rou-model-metadata-workflow.effects';
import { RouInitialDirectCostWorkflowEffects } from './effects/rou-initial-direct-cost-workflow.effects';
import { LeaseTemplateWorkflowEffects } from './effects/lease-template-workflow.effects';
import * as fromIfrs16LeaseModelUpdates from "./reducers/ifrs16-lease-model-workflow-status.reducer";
import * as fromTAAmortizationUpdates from "./reducers/ta-amortization-rule-status.reducer";
import * as fromTAInterestPaidTransferRuleUpdates from "./reducers/ta-interest-paid-transfer-rule-status.reducer";
import * as fromTALeaseInterestAccrualRuleUpdates from "./reducers/ta-lease-interest-accrual-rule-status.reducer";
import * as fromTALeaseRecognitionRuleUpdates from "./reducers/ta-lease-recognition-rule-status.reducer";
import * as fromTALeaseRepaymentRuleUpdates from "./reducers/ta-lease-repayment-rule-status.reducer";
import * as fromTARecognitionRouRuleUpdates from "./reducers/ta-recognition-rou-rule-status.reducer";
import * as fromTransactionAccountUpdates from "./reducers/transaction-account-update-status.reducer";
import * as fromTransactionAccountReportDateUpdates from "./reducers/transaction-account-report-date-selection.reducer";
import * as fromLeaseAmortizationCalculationState from "./reducers/lease-amortization-calculation.reducer";
import * as fromLeaseLiabilityState from "./reducers/lease-liability.reducer";
import * as fromLeasePaymentState from "./reducers/lease-payment.reducer";
import * as fromRouModelMetadataUpdates from "./reducers/rou-model-metadata-workflow-status.reducer";
import * as fromRouInitialDirectCostUpdates from "./reducers/rou-initial-direct-cost-workflow-status.reducer";
import * as fromLeaseTemplateUpdates from "./reducers/lease-template-workflow-status.reducer";
import * as fromLeasePeriodIdSelectionUpdates from "./reducers/lease-period-selection-workflow-status.reducer";
import * as fromLeasePeriodReportPathUpdates from "./reducers/lease-period-report-path-selection.reducer";
import { PrepaymentMarshallingWorkflowEffects } from './effects/prepayment-marshalling-workflow.effects';
import * as fromIfrs16LeaseContractReport from "./reducers/ifrs16-lease-contract-report.reducer";

@NgModule({
  imports: [
    EffectsModule.forRoot([
      SettlementUpdatesEffects,
      PrepaymentAccountWorkflowEffects,
      DealerInvoiceWorkflowEffects,
      PrepaymentMarshallingWorkflowEffects,
      Ifrs16LeaseModelWorkflowEffects,
      RouModelMetadataWorkflowEffects,
      RouInitialDirectCostWorkflowEffects,
      LeaseTemplateWorkflowEffects,
      DealerPaymentsEffects]),
    EffectsModule.forFeature([]),
    StoreModule.forRoot({}, {runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: false,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true,
      }}),
    StoreDevtoolsModule.instrument({
      name: 'ERP App States',
      maxAge: 100, // Retains last 100 states
    }),
    StoreModule.forFeature('recordDealerInvoiceWorkflows', fromDealerInvoiceWorkflows.dealerInvoiceWorkflowStateReducer),
    StoreModule.forFeature('paymentToDealerWorkflows', fromDealerWorkflows.dealerWorkflowStateReducer),
    StoreModule.forFeature('paymentUpdateForm', fromPaymentUpdates.paymentUpdateStateReducer),
    StoreModule.forFeature('settlementUpdateForm', fromSettlementUpdates.settlementUpdateStateReducer),
    StoreModule.forFeature('assetRegistrationUpdateForm', fromAssetRegistrationUpdates.assetRegistrationUpdateStateReducer),
    StoreModule.forFeature('assetAccessoryUpdateForm', fromAssetAccessoryUpdates.assetAccessoryUpdateStateReducer),
    StoreModule.forFeature('wipRegistrationUpdateForm', fromWIPRegistrationUpdates.wipRegistrationUpdateStateReducer),
    StoreModule.forFeature('paymentInvoiceUpdateForm', fromPaymentInvoiceUpdates.paymentInvoiceUpdateStateReducer),
    StoreModule.forFeature('reportNavigationProfileState', fromReportNavigationProfile.reportNavigationProfileStateReducer),
    StoreModule.forFeature('prepaymentAccountUpdateForm', fromPrepaymentAccountUpdates.prepaymentAccountUpdateStateReducer),
    StoreModule.forFeature('prepaymentMarshallingUpdateForm', fromPrepaymentMarshallingUpdates.prepaymentMarshallingUpdateStateReducer),
    StoreModule.forFeature('ifrs16LeaseModelUpdateForm', fromIfrs16LeaseModelUpdates.ifrs16LeaseModelUpdateStateReducer),
    StoreModule.forFeature('taAmortizationRuleUpdateForm', fromTAAmortizationUpdates.taAmortizationRuleUpdateStateReducer),
    StoreModule.forFeature('taInterestPaidTransferRuleUpdateForm', fromTAInterestPaidTransferRuleUpdates.taInterestPaidTransferRuleUpdateStateReducer),
    StoreModule.forFeature('taLeaseInterestAccrualRuleUpdateForm', fromTALeaseInterestAccrualRuleUpdates.taLeaseInterestAccrualRuleUpdateStateReducer),
    StoreModule.forFeature('taLeaseRecognitionRuleUpdateForm', fromTALeaseRecognitionRuleUpdates.taLeaseRecognitionRuleUpdateStateReducer),
    StoreModule.forFeature('taLeaseRepaymentRuleUpdateForm', fromTALeaseRepaymentRuleUpdates.taLeaseRepaymentRuleUpdateStateReducer),
    StoreModule.forFeature('taRecognitionRouRuleUpdateForm', fromTARecognitionRouRuleUpdates.taRecognitionRouRuleUpdateStateReducer),
    StoreModule.forFeature('transactionAccountUpdateForm', fromTransactionAccountUpdates.transactionAccountUpdateStateReducer),
    StoreModule.forFeature('transactionAccountReportDateSelection', fromTransactionAccountReportDateUpdates.transactionAccountReportPathSelectionStateReducer),
    StoreModule.forFeature('leaseAmortizationCalculationForm', fromLeaseAmortizationCalculationState.leaseAmortizationCalculationStateReducer),
    StoreModule.forFeature('leaseLiabilityForm', fromLeaseLiabilityState.leaseLiabilityStateReducer),
    StoreModule.forFeature('leasePaymentForm', fromLeasePaymentState.leasePaymentStateReducer),
    StoreModule.forFeature('rouModelMetadataUpdateForm', fromRouModelMetadataUpdates.rouModelMetadataUpdateStateReducer),
    StoreModule.forFeature('rouInitialDirectCostUpdateForm', fromRouInitialDirectCostUpdates.rouInitialDirectCostUpdateStateReducer),
    StoreModule.forFeature('leaseTemplateUpdateForm', fromLeaseTemplateUpdates.leaseTemplateUpdateStateReducer),
    StoreModule.forFeature('leasePeriodIdSelection', fromLeasePeriodIdSelectionUpdates.leasePeriodIdSelectionStateReducer),
    StoreModule.forFeature('leasePeriodReportPath', fromLeasePeriodReportPathUpdates.leasePeriodReportPathSelectionStateReducer),
    StoreModule.forFeature('ifrs16LeaseContractReport', fromIfrs16LeaseContractReport.ifrs16LeaseContractReportStateReducer),
  ],
  exports: [
    EffectsModule,
    StoreModule,
    StoreDevtoolsModule,
  ]
})
export class ErpStoreModule {}
