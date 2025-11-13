import { initialState, State } from '../global-store.definition';
import { taInterestPaidTransferRuleSelectedInstance, editingTAInterestPaidTransferRuleStatus, creatingTAInterestPaidTransferRuleStatus, copyingTAInterestPaidTransferRuleStatus } from './ta-interest-paid-transfer-rule-status.selectors';
import { taLeaseInterestAccrualRuleSelectedInstance, editingTALeaseInterestAccrualRuleStatus, creatingTALeaseInterestAccrualRuleStatus, copyingTALeaseInterestAccrualRuleStatus } from './ta-lease-interest-accrual-rule-status.selectors';
import { taLeaseRecognitionRuleSelectedInstance, editingTALeaseRecognitionRuleStatus, creatingTALeaseRecognitionRuleStatus, copyingTALeaseRecognitionRuleStatus } from './ta-lease-recognition-rule-status.selectors';
import { taLeaseRepaymentRuleSelectedInstance, editingTALeaseRepaymentRuleStatus, creatingTALeaseRepaymentRuleStatus, copyingTALeaseRepaymentRuleStatus } from './ta-lease-repayment-rule-status.selectors';
import { taRecognitionRouRuleSelectedInstance, editingTARecognitionRouRuleStatus, creatingTARecognitionRouRuleStatus, copyingTARecognitionRouRuleStatus } from './ta-recognition-rou-rule-status.selectors';
import { ITAInterestPaidTransferRule } from '../../erp-accounts/ta-interest-paid-transfer-rule/ta-interest-paid-transfer-rule.model';
import { ITALeaseInterestAccrualRule } from '../../erp-accounts/ta-lease-interest-accrual-rule/ta-lease-interest-accrual-rule.model';
import { ITALeaseRecognitionRule } from '../../erp-accounts/ta-lease-recognition-rule/ta-lease-recognition-rule.model';
import { ITALeaseRepaymentRule } from '../../erp-accounts/ta-lease-repayment-rule/ta-lease-repayment-rule.model';
import { ITARecognitionROURule } from '../../erp-accounts/ta-recognition-rou-rule/ta-recognition-rou-rule.model';

describe('TA rule update selectors', () => {
  const taInterestPaidTransferRuleSample = { id: 201 } as ITAInterestPaidTransferRule;
  const taLeaseInterestAccrualRuleSample = { id: 202 } as ITALeaseInterestAccrualRule;
  const taLeaseRecognitionRuleSample = { id: 203 } as ITALeaseRecognitionRule;
  const taLeaseRepaymentRuleSample = { id: 204 } as ITALeaseRepaymentRule;
  const taRecognitionRouRuleSample = { id: 205 } as ITARecognitionROURule;

  it('should select TA Interest Paid Transfer Rule workflow data', () => {
    const featureState: State = {
      ...initialState,
      taInterestPaidTransferRuleFormState: {
        ...initialState.taInterestPaidTransferRuleFormState,
        selectedInstance: taInterestPaidTransferRuleSample,
        weAreCopying: true,
        weAreEditing: false,
        weAreCreating: false,
      },
    };

    expect(taInterestPaidTransferRuleSelectedInstance.projector(featureState)).toBe(taInterestPaidTransferRuleSample);
    expect(editingTAInterestPaidTransferRuleStatus.projector(featureState)).toBe(false);
    expect(creatingTAInterestPaidTransferRuleStatus.projector(featureState)).toBe(false);
    expect(copyingTAInterestPaidTransferRuleStatus.projector(featureState)).toBe(true);
  });

  it('should select TA Lease Interest Accrual Rule workflow data', () => {
    const featureState: State = {
      ...initialState,
      taLeaseInterestAccrualRuleFormState: {
        ...initialState.taLeaseInterestAccrualRuleFormState,
        selectedInstance: taLeaseInterestAccrualRuleSample,
        weAreCopying: false,
        weAreEditing: true,
        weAreCreating: false,
      },
    };

    expect(taLeaseInterestAccrualRuleSelectedInstance.projector(featureState)).toBe(taLeaseInterestAccrualRuleSample);
    expect(editingTALeaseInterestAccrualRuleStatus.projector(featureState)).toBe(true);
    expect(creatingTALeaseInterestAccrualRuleStatus.projector(featureState)).toBe(false);
    expect(copyingTALeaseInterestAccrualRuleStatus.projector(featureState)).toBe(false);
  });

  it('should select TA Lease Recognition Rule workflow data', () => {
    const featureState: State = {
      ...initialState,
      taLeaseRecognitionRuleFormState: {
        ...initialState.taLeaseRecognitionRuleFormState,
        selectedInstance: taLeaseRecognitionRuleSample,
        weAreCopying: false,
        weAreEditing: false,
        weAreCreating: true,
      },
    };

    expect(taLeaseRecognitionRuleSelectedInstance.projector(featureState)).toBe(taLeaseRecognitionRuleSample);
    expect(editingTALeaseRecognitionRuleStatus.projector(featureState)).toBe(false);
    expect(creatingTALeaseRecognitionRuleStatus.projector(featureState)).toBe(true);
    expect(copyingTALeaseRecognitionRuleStatus.projector(featureState)).toBe(false);
  });

  it('should select TA Lease Repayment Rule workflow data', () => {
    const featureState: State = {
      ...initialState,
      taLeaseRepaymentRuleFormState: {
        ...initialState.taLeaseRepaymentRuleFormState,
        selectedInstance: taLeaseRepaymentRuleSample,
        weAreCopying: false,
        weAreEditing: true,
        weAreCreating: false,
      },
    };

    expect(taLeaseRepaymentRuleSelectedInstance.projector(featureState)).toBe(taLeaseRepaymentRuleSample);
    expect(editingTALeaseRepaymentRuleStatus.projector(featureState)).toBe(true);
    expect(creatingTALeaseRepaymentRuleStatus.projector(featureState)).toBe(false);
    expect(copyingTALeaseRepaymentRuleStatus.projector(featureState)).toBe(false);
  });

  it('should select TA Recognition ROU Rule workflow data', () => {
    const featureState: State = {
      ...initialState,
      taRecognitionRouRuleFormState: {
        ...initialState.taRecognitionRouRuleFormState,
        selectedInstance: taRecognitionRouRuleSample,
        weAreCopying: true,
        weAreEditing: false,
        weAreCreating: false,
      },
    };

    expect(taRecognitionRouRuleSelectedInstance.projector(featureState)).toBe(taRecognitionRouRuleSample);
    expect(editingTARecognitionRouRuleStatus.projector(featureState)).toBe(false);
    expect(creatingTARecognitionRouRuleStatus.projector(featureState)).toBe(false);
    expect(copyingTARecognitionRouRuleStatus.projector(featureState)).toBe(true);
  });
});
