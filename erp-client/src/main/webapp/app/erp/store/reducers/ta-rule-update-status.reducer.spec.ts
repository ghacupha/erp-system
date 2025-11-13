import { Action } from '@ngrx/store';
import { initialState } from '../global-store.definition';
import {
  taInterestPaidTransferRuleCopyWorkflowInitiatedFromList,
  taInterestPaidTransferRuleCreationInitiatedFromList,
  taInterestPaidTransferRuleEditWorkflowInitiatedFromView,
  taInterestPaidTransferRuleUpdateFormHasBeenDestroyed,
  taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/ta-interest-paid-transfer-rule-update-status.actions';
import { taInterestPaidTransferRuleUpdateStateReducer } from './ta-interest-paid-transfer-rule-status.reducer';
import {
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleCreationInitiatedFromList,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView,
  taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed,
  taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/ta-lease-interest-accrual-rule-update-status.actions';
import { taLeaseInterestAccrualRuleUpdateStateReducer } from './ta-lease-interest-accrual-rule-status.reducer';
import {
  taLeaseRecognitionRuleCopyWorkflowInitiatedFromList,
  taLeaseRecognitionRuleCreationInitiatedFromList,
  taLeaseRecognitionRuleEditWorkflowInitiatedFromView,
  taLeaseRecognitionRuleUpdateFormHasBeenDestroyed,
  taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/ta-lease-recognition-rule-update-status.actions';
import { taLeaseRecognitionRuleUpdateStateReducer } from './ta-lease-recognition-rule-status.reducer';
import {
  taLeaseRepaymentRuleCopyWorkflowInitiatedFromList,
  taLeaseRepaymentRuleCreationInitiatedFromList,
  taLeaseRepaymentRuleEditWorkflowInitiatedFromView,
  taLeaseRepaymentRuleUpdateFormHasBeenDestroyed,
  taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/ta-lease-repayment-rule-update-status.actions';
import { taLeaseRepaymentRuleUpdateStateReducer } from './ta-lease-repayment-rule-status.reducer';
import {
  taRecognitionRouRuleCopyWorkflowInitiatedFromList,
  taRecognitionRouRuleCreationInitiatedFromList,
  taRecognitionRouRuleEditWorkflowInitiatedFromView,
  taRecognitionRouRuleUpdateFormHasBeenDestroyed,
  taRecognitionRouRuleUpdateInstanceAcquiredFromBackend,
} from '../actions/ta-recognition-rou-rule-update-status.actions';
import { taRecognitionRouRuleUpdateStateReducer } from './ta-recognition-rou-rule-status.reducer';
import { ITAInterestPaidTransferRule } from '../../erp-accounts/ta-interest-paid-transfer-rule/ta-interest-paid-transfer-rule.model';
import { ITALeaseInterestAccrualRule } from '../../erp-accounts/ta-lease-interest-accrual-rule/ta-lease-interest-accrual-rule.model';
import { ITALeaseRecognitionRule } from '../../erp-accounts/ta-lease-recognition-rule/ta-lease-recognition-rule.model';
import { ITALeaseRepaymentRule } from '../../erp-accounts/ta-lease-repayment-rule/ta-lease-repayment-rule.model';
import { ITARecognitionROURule } from '../../erp-accounts/ta-recognition-rou-rule/ta-recognition-rou-rule.model';

describe('TA rule update reducers', () => {
  const taInterestPaidTransferRuleSample = { id: 101 } as ITAInterestPaidTransferRule;
  const taLeaseInterestAccrualRuleSample = { id: 102 } as ITALeaseInterestAccrualRule;
  const taLeaseRecognitionRuleSample = { id: 103 } as ITALeaseRecognitionRule;
  const taLeaseRepaymentRuleSample = { id: 104 } as ITALeaseRepaymentRule;
  const taRecognitionRouRuleSample = { id: 105 } as ITARecognitionROURule;

  const unknownAction = { type: 'Unknown Action' } as Action;

  it('should preserve the initial state when an unknown action is dispatched', () => {
    expect(taInterestPaidTransferRuleUpdateStateReducer(undefined, unknownAction)).toBe(initialState);
    expect(taLeaseInterestAccrualRuleUpdateStateReducer(undefined, unknownAction)).toBe(initialState);
    expect(taLeaseRecognitionRuleUpdateStateReducer(undefined, unknownAction)).toBe(initialState);
    expect(taLeaseRepaymentRuleUpdateStateReducer(undefined, unknownAction)).toBe(initialState);
    expect(taRecognitionRouRuleUpdateStateReducer(undefined, unknownAction)).toBe(initialState);
  });

  it('should set the TA Interest Paid Transfer Rule state for create, copy, edit and backend acquisition workflows', () => {
    const createState = taInterestPaidTransferRuleUpdateStateReducer(
      undefined,
      taInterestPaidTransferRuleCreationInitiatedFromList()
    );
    expect(createState.taInterestPaidTransferRuleFormState).toEqual({
      ...initialState.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
      weAreCreating: true,
      weAreCopying: false,
      weAreEditing: false,
    });

    const copyState = taInterestPaidTransferRuleUpdateStateReducer(
      undefined,
      taInterestPaidTransferRuleCopyWorkflowInitiatedFromList({ copiedInstance: taInterestPaidTransferRuleSample })
    );
    expect(copyState.taInterestPaidTransferRuleFormState).toEqual({
      ...initialState.taInterestPaidTransferRuleFormState,
      selectedInstance: taInterestPaidTransferRuleSample,
      weAreCreating: false,
      weAreCopying: true,
      weAreEditing: false,
    });

    const editState = taInterestPaidTransferRuleUpdateStateReducer(
      undefined,
      taInterestPaidTransferRuleEditWorkflowInitiatedFromView({ editedInstance: taInterestPaidTransferRuleSample })
    );
    expect(editState.taInterestPaidTransferRuleFormState).toEqual({
      ...initialState.taInterestPaidTransferRuleFormState,
      selectedInstance: taInterestPaidTransferRuleSample,
      weAreCreating: false,
      weAreCopying: false,
      weAreEditing: true,
    });

    const backendState = taInterestPaidTransferRuleUpdateStateReducer(
      undefined,
      taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(backendState.taInterestPaidTransferRuleFormState).toEqual({
      ...initialState.taInterestPaidTransferRuleFormState,
      selectedInstance: taInterestPaidTransferRuleSample,
      backEndFetchComplete: true,
    });

    const destroyedState = taInterestPaidTransferRuleUpdateStateReducer(
      backendState,
      taInterestPaidTransferRuleUpdateFormHasBeenDestroyed()
    );
    expect(destroyedState.taInterestPaidTransferRuleFormState).toEqual({
      ...initialState.taInterestPaidTransferRuleFormState,
      selectedInstance: {},
    });
  });

  it('should set the TA Lease Interest Accrual Rule state for create, copy, edit and backend acquisition workflows', () => {
    const createState = taLeaseInterestAccrualRuleUpdateStateReducer(
      undefined,
      taLeaseInterestAccrualRuleCreationInitiatedFromList()
    );
    expect(createState.taLeaseInterestAccrualRuleFormState).toEqual({
      ...initialState.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
      weAreCreating: true,
      weAreCopying: false,
      weAreEditing: false,
    });

    const copyState = taLeaseInterestAccrualRuleUpdateStateReducer(
      undefined,
      taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseInterestAccrualRuleSample })
    );
    expect(copyState.taLeaseInterestAccrualRuleFormState).toEqual({
      ...initialState.taLeaseInterestAccrualRuleFormState,
      selectedInstance: taLeaseInterestAccrualRuleSample,
      weAreCreating: false,
      weAreCopying: true,
      weAreEditing: false,
    });

    const editState = taLeaseInterestAccrualRuleUpdateStateReducer(
      undefined,
      taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseInterestAccrualRuleSample })
    );
    expect(editState.taLeaseInterestAccrualRuleFormState).toEqual({
      ...initialState.taLeaseInterestAccrualRuleFormState,
      selectedInstance: taLeaseInterestAccrualRuleSample,
      weAreCreating: false,
      weAreCopying: false,
      weAreEditing: true,
    });

    const backendState = taLeaseInterestAccrualRuleUpdateStateReducer(
      undefined,
      taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(backendState.taLeaseInterestAccrualRuleFormState).toEqual({
      ...initialState.taLeaseInterestAccrualRuleFormState,
      selectedInstance: taLeaseInterestAccrualRuleSample,
      backEndFetchComplete: true,
    });

    const destroyedState = taLeaseInterestAccrualRuleUpdateStateReducer(
      backendState,
      taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed()
    );
    expect(destroyedState.taLeaseInterestAccrualRuleFormState).toEqual({
      ...initialState.taLeaseInterestAccrualRuleFormState,
      selectedInstance: {},
    });
  });

  it('should set the TA Lease Recognition Rule state for create, copy, edit and backend acquisition workflows', () => {
    const createState = taLeaseRecognitionRuleUpdateStateReducer(
      undefined,
      taLeaseRecognitionRuleCreationInitiatedFromList()
    );
    expect(createState.taLeaseRecognitionRuleFormState).toEqual({
      ...initialState.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
      weAreCreating: true,
      weAreCopying: false,
      weAreEditing: false,
    });

    const copyState = taLeaseRecognitionRuleUpdateStateReducer(
      undefined,
      taLeaseRecognitionRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseRecognitionRuleSample })
    );
    expect(copyState.taLeaseRecognitionRuleFormState).toEqual({
      ...initialState.taLeaseRecognitionRuleFormState,
      selectedInstance: taLeaseRecognitionRuleSample,
      weAreCreating: false,
      weAreCopying: true,
      weAreEditing: false,
    });

    const editState = taLeaseRecognitionRuleUpdateStateReducer(
      undefined,
      taLeaseRecognitionRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseRecognitionRuleSample })
    );
    expect(editState.taLeaseRecognitionRuleFormState).toEqual({
      ...initialState.taLeaseRecognitionRuleFormState,
      selectedInstance: taLeaseRecognitionRuleSample,
      weAreCreating: false,
      weAreCopying: false,
      weAreEditing: true,
    });

    const backendState = taLeaseRecognitionRuleUpdateStateReducer(
      undefined,
      taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(backendState.taLeaseRecognitionRuleFormState).toEqual({
      ...initialState.taLeaseRecognitionRuleFormState,
      selectedInstance: taLeaseRecognitionRuleSample,
      backEndFetchComplete: true,
    });

    const destroyedState = taLeaseRecognitionRuleUpdateStateReducer(
      backendState,
      taLeaseRecognitionRuleUpdateFormHasBeenDestroyed()
    );
    expect(destroyedState.taLeaseRecognitionRuleFormState).toEqual({
      ...initialState.taLeaseRecognitionRuleFormState,
      selectedInstance: {},
    });
  });

  it('should set the TA Lease Repayment Rule state for create, copy, edit and backend acquisition workflows', () => {
    const createState = taLeaseRepaymentRuleUpdateStateReducer(
      undefined,
      taLeaseRepaymentRuleCreationInitiatedFromList()
    );
    expect(createState.taLeaseRepaymentRuleFormState).toEqual({
      ...initialState.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
      weAreCreating: true,
      weAreCopying: false,
      weAreEditing: false,
    });

    const copyState = taLeaseRepaymentRuleUpdateStateReducer(
      undefined,
      taLeaseRepaymentRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseRepaymentRuleSample })
    );
    expect(copyState.taLeaseRepaymentRuleFormState).toEqual({
      ...initialState.taLeaseRepaymentRuleFormState,
      selectedInstance: taLeaseRepaymentRuleSample,
      weAreCreating: false,
      weAreCopying: true,
      weAreEditing: false,
    });

    const editState = taLeaseRepaymentRuleUpdateStateReducer(
      undefined,
      taLeaseRepaymentRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseRepaymentRuleSample })
    );
    expect(editState.taLeaseRepaymentRuleFormState).toEqual({
      ...initialState.taLeaseRepaymentRuleFormState,
      selectedInstance: taLeaseRepaymentRuleSample,
      weAreCreating: false,
      weAreCopying: false,
      weAreEditing: true,
    });

    const backendState = taLeaseRepaymentRuleUpdateStateReducer(
      undefined,
      taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(backendState.taLeaseRepaymentRuleFormState).toEqual({
      ...initialState.taLeaseRepaymentRuleFormState,
      selectedInstance: taLeaseRepaymentRuleSample,
      backEndFetchComplete: true,
    });

    const destroyedState = taLeaseRepaymentRuleUpdateStateReducer(
      backendState,
      taLeaseRepaymentRuleUpdateFormHasBeenDestroyed()
    );
    expect(destroyedState.taLeaseRepaymentRuleFormState).toEqual({
      ...initialState.taLeaseRepaymentRuleFormState,
      selectedInstance: {},
    });
  });

  it('should set the TA Recognition ROU Rule state for create, copy, edit and backend acquisition workflows', () => {
    const createState = taRecognitionRouRuleUpdateStateReducer(
      undefined,
      taRecognitionRouRuleCreationInitiatedFromList()
    );
    expect(createState.taRecognitionRouRuleFormState).toEqual({
      ...initialState.taRecognitionRouRuleFormState,
      selectedInstance: {},
      weAreCreating: true,
      weAreCopying: false,
      weAreEditing: false,
    });

    const copyState = taRecognitionRouRuleUpdateStateReducer(
      undefined,
      taRecognitionRouRuleCopyWorkflowInitiatedFromList({ copiedInstance: taRecognitionRouRuleSample })
    );
    expect(copyState.taRecognitionRouRuleFormState).toEqual({
      ...initialState.taRecognitionRouRuleFormState,
      selectedInstance: taRecognitionRouRuleSample,
      weAreCreating: false,
      weAreCopying: true,
      weAreEditing: false,
    });

    const editState = taRecognitionRouRuleUpdateStateReducer(
      undefined,
      taRecognitionRouRuleEditWorkflowInitiatedFromView({ editedInstance: taRecognitionRouRuleSample })
    );
    expect(editState.taRecognitionRouRuleFormState).toEqual({
      ...initialState.taRecognitionRouRuleFormState,
      selectedInstance: taRecognitionRouRuleSample,
      weAreCreating: false,
      weAreCopying: false,
      weAreEditing: true,
    });

    const backendState = taRecognitionRouRuleUpdateStateReducer(
      undefined,
      taRecognitionRouRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taRecognitionRouRuleSample,
      })
    );
    expect(backendState.taRecognitionRouRuleFormState).toEqual({
      ...initialState.taRecognitionRouRuleFormState,
      selectedInstance: taRecognitionRouRuleSample,
      backEndFetchComplete: true,
    });

    const destroyedState = taRecognitionRouRuleUpdateStateReducer(
      backendState,
      taRecognitionRouRuleUpdateFormHasBeenDestroyed()
    );
    expect(destroyedState.taRecognitionRouRuleFormState).toEqual({
      ...initialState.taRecognitionRouRuleFormState,
      selectedInstance: {},
    });
  });
});
