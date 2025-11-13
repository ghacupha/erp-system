///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import {PaymentsFormState} from "./reducers/update-menu-status.reducer";
import {DealerWorkflowState} from "./reducers/dealer-workflows-status.reducer";
import {DealerInvoiceWorkflowState} from "./reducers/dealer-invoice-workflows-status.reducer";
import { SettlementsFormState } from './reducers/settlement-update-menu-status.reducer';
import { AssetRegistrationFormState } from './reducers/asset-registration-workflow-status.reducer';
import { AssetAccessoryFormState } from './reducers/asset-accessory-workflow-status.reducer';
import { WIPRegistrationFormState } from './reducers/wip-registration-workflow-status.reducer';
import { PrepaymentAccountFormState } from './reducers/prepayment-account-workflow-status.reducer';
import { PrepaymentMarshallingFormState } from './reducers/prepayment-marshalling-workflow-status.reducer';
import { PaymentInvoiceFormState } from './reducers/payment-invoice-workflow-status.reducer';
import { ReportNavigationProfileState } from './reducers/report-navigation-profile-status.reducer';
import dayjs from 'dayjs';
import { DATE_FORMAT } from '../../config/input.constants';
import { IFRS16LeaseModelFormState } from './reducers/ifrs16-lease-model-workflow-status.reducer';
import { RouModelMetadataFormState } from './reducers/rou-model-metadata-workflow-status.reducer';
import { LeasePeriodSelectionFormState } from './reducers/lease-period-selection-workflow-status.reducer';
import { LeasePeriodReportPathSelectionState } from './reducers/lease-period-report-path-selection.reducer';
import { LeaseAmortizationCalculationFormState } from './reducers/lease-amortization-calculation.reducer';
import { LeaseLiabilityFormState } from './reducers/lease-liability.reducer';
import { LeasePaymentFormState } from './reducers/lease-payment.reducer';
import { TAAmortizationRuleFormState } from './reducers/ta-amortization-rule-status.reducer';
import { TAInterestPaidTransferRuleFormState } from './reducers/ta-interest-paid-transfer-rule-status.reducer';
import { TALeaseInterestAccrualRuleFormState } from './reducers/ta-lease-interest-accrual-rule-status.reducer';
import { TALeaseRecognitionRuleFormState } from './reducers/ta-lease-recognition-rule-status.reducer';
import { TALeaseRepaymentRuleFormState } from './reducers/ta-lease-repayment-rule-status.reducer';
import { TARecognitionROURuleFormState } from './reducers/ta-recognition-rou-rule-status.reducer';
import { TransactionAccountFormState } from './reducers/transaction-account-update-status.reducer';
import { TransactionAccountReportDateSelectionState } from './reducers/transaction-account-report-date-selection.reducer';
import { IFRS16LeaseContractReportState } from './reducers/ifrs16-lease-contract-report.reducer';

export interface State {
  paymentsFormState: PaymentsFormState,
  settlementsFormState: SettlementsFormState,
  dealerWorkflowState: DealerWorkflowState,
  dealerInvoiceWorkflowState: DealerInvoiceWorkflowState,
  assetRegistrationFormState: AssetRegistrationFormState,
  assetAccessoryFormState: AssetAccessoryFormState,
  wipRegistrationFormState: WIPRegistrationFormState,
  prepaymentAccountFormState: PrepaymentAccountFormState,
  prepaymentMarshallingFormState: PrepaymentMarshallingFormState,
  paymentInvoiceFormState: PaymentInvoiceFormState,
  reportNavigationProfileState: ReportNavigationProfileState
  ifrs16LeaseModelFormState: IFRS16LeaseModelFormState,
  leaseAmortizationCalculationFormState: LeaseAmortizationCalculationFormState,
  leaseLiabilityFormState: LeaseLiabilityFormState,
  leasePaymentFormState: LeasePaymentFormState,
  rouModelMetadataFormState: RouModelMetadataFormState,
  leasePeriodSelectionIdFormState: LeasePeriodSelectionFormState,
  leasePeriodReportPathSelectionState: LeasePeriodReportPathSelectionState,
  taAmortizationRuleFormState: TAAmortizationRuleFormState,
  taInterestPaidTransferRuleFormState: TAInterestPaidTransferRuleFormState,
  taLeaseInterestAccrualRuleFormState: TALeaseInterestAccrualRuleFormState,
  taLeaseRecognitionRuleFormState: TALeaseRecognitionRuleFormState,
  taLeaseRepaymentRuleFormState: TALeaseRepaymentRuleFormState,
  taRecognitionRouRuleFormState: TARecognitionROURuleFormState,
  transactionAccountFormState: TransactionAccountFormState,
  transactionAccountReportDateSelectionState: TransactionAccountReportDateSelectionState,
  ifrs16LeaseContractReportState: IFRS16LeaseContractReportState
}

export const initialState: State = {
  paymentsFormState: {
    selectedPayment: {},
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  settlementsFormState: {
    selectedSettlement: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  assetRegistrationFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  wipRegistrationFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  prepaymentAccountFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  prepaymentMarshallingFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  ifrs16LeaseModelFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taAmortizationRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taInterestPaidTransferRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taLeaseInterestAccrualRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taLeaseRecognitionRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taLeaseRepaymentRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  taRecognitionRouRuleFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  transactionAccountFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  leaseAmortizationCalculationFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  leaseLiabilityFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  leasePaymentFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  rouModelMetadataFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  assetAccessoryFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  paymentInvoiceFormState: {
    selectedInstance: {},
    browserHasBeenRefreshed: false,
    backEndFetchComplete: false,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  dealerWorkflowState: {
    selectedDealer: {},
    dealerPayment: {},
    paymentDealerCategory: {},
    weArePayingADealer: false,
    errorMessage: '',
  },
  dealerInvoiceWorkflowState: {
    invoiceDealer: {},
    selectedInvoice: {},
    invoicePayment: {},
    selectedInvoiceLabels: [],
    selectedInvoicePlaceholders: [],
    weArePayingAnInvoiceDealer: false,
    errorMessage: '',
  },
  reportNavigationProfileState: {
    reportPath: '',
    reportTitle: 'ERP Reports',
    reportDate: dayjs().format(DATE_FORMAT)
  },
  leasePeriodSelectionIdFormState: {
    selectedLeasePeriodId: undefined,
    weAreCopying: false,
    weAreEditing: false,
    weAreCreating: false,
  },
  leasePeriodReportPathSelectionState: {
    leasePeriodReportPath: '',
    leasePeriodReportTitle: '',
  },
  transactionAccountReportDateSelectionState: {
    transactionAccountReportPath: '',
    transactionAccountReportTitle: '',
    transactionAccountReportDate: dayjs(),
  },
  ifrs16LeaseContractReportState: {
    selectedLeaseContractId: undefined,
  }
}
