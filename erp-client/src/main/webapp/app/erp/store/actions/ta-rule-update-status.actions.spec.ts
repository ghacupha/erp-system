import {
  taInterestPaidTransferRuleCopyWorkflowInitiatedEnRoute,
  taInterestPaidTransferRuleCopyWorkflowInitiatedFromList,
  taInterestPaidTransferRuleCopyWorkflowInitiatedFromView,
  taInterestPaidTransferRuleCreationInitiatedEnRoute,
  taInterestPaidTransferRuleCreationInitiatedFromList,
  taInterestPaidTransferRuleCreationWorkflowInitiatedFromList,
  taInterestPaidTransferRuleDataHasMutated,
  taInterestPaidTransferRuleEditWorkflowInitiatedEnRoute,
  taInterestPaidTransferRuleEditWorkflowInitiatedFromList,
  taInterestPaidTransferRuleEditWorkflowInitiatedFromView,
  taInterestPaidTransferRuleUpdateFormHasBeenDestroyed,
  taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend,
  taInterestPaidTransferRuleUpdateInstanceAcquisitionFromBackendFailed,
} from './ta-interest-paid-transfer-rule-update-status.actions';
import {
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedEnRoute,
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromView,
  taLeaseInterestAccrualRuleCreationInitiatedEnRoute,
  taLeaseInterestAccrualRuleCreationInitiatedFromList,
  taLeaseInterestAccrualRuleCreationWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleDataHasMutated,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedEnRoute,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedFromList,
  taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView,
  taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed,
  taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend,
  taLeaseInterestAccrualRuleUpdateInstanceAcquisitionFromBackendFailed,
} from './ta-lease-interest-accrual-rule-update-status.actions';
import {
  taLeaseRecognitionRuleCopyWorkflowInitiatedEnRoute,
  taLeaseRecognitionRuleCopyWorkflowInitiatedFromList,
  taLeaseRecognitionRuleCopyWorkflowInitiatedFromView,
  taLeaseRecognitionRuleCreationInitiatedEnRoute,
  taLeaseRecognitionRuleCreationInitiatedFromList,
  taLeaseRecognitionRuleCreationWorkflowInitiatedFromList,
  taLeaseRecognitionRuleDataHasMutated,
  taLeaseRecognitionRuleEditWorkflowInitiatedEnRoute,
  taLeaseRecognitionRuleEditWorkflowInitiatedFromList,
  taLeaseRecognitionRuleEditWorkflowInitiatedFromView,
  taLeaseRecognitionRuleUpdateFormHasBeenDestroyed,
  taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend,
  taLeaseRecognitionRuleUpdateInstanceAcquisitionFromBackendFailed,
} from './ta-lease-recognition-rule-update-status.actions';
import {
  taLeaseRepaymentRuleCopyWorkflowInitiatedEnRoute,
  taLeaseRepaymentRuleCopyWorkflowInitiatedFromList,
  taLeaseRepaymentRuleCopyWorkflowInitiatedFromView,
  taLeaseRepaymentRuleCreationInitiatedEnRoute,
  taLeaseRepaymentRuleCreationInitiatedFromList,
  taLeaseRepaymentRuleCreationWorkflowInitiatedFromList,
  taLeaseRepaymentRuleDataHasMutated,
  taLeaseRepaymentRuleEditWorkflowInitiatedEnRoute,
  taLeaseRepaymentRuleEditWorkflowInitiatedFromList,
  taLeaseRepaymentRuleEditWorkflowInitiatedFromView,
  taLeaseRepaymentRuleUpdateFormHasBeenDestroyed,
  taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend,
  taLeaseRepaymentRuleUpdateInstanceAcquisitionFromBackendFailed,
} from './ta-lease-repayment-rule-update-status.actions';
import {
  taRecognitionRouRuleCopyWorkflowInitiatedEnRoute,
  taRecognitionRouRuleCopyWorkflowInitiatedFromList,
  taRecognitionRouRuleCopyWorkflowInitiatedFromView,
  taRecognitionRouRuleCreationInitiatedEnRoute,
  taRecognitionRouRuleCreationInitiatedFromList,
  taRecognitionRouRuleCreationWorkflowInitiatedFromList,
  taRecognitionRouRuleDataHasMutated,
  taRecognitionRouRuleEditWorkflowInitiatedEnRoute,
  taRecognitionRouRuleEditWorkflowInitiatedFromList,
  taRecognitionRouRuleEditWorkflowInitiatedFromView,
  taRecognitionRouRuleUpdateFormHasBeenDestroyed,
  taRecognitionRouRuleUpdateInstanceAcquiredFromBackend,
  taRecognitionRouRuleUpdateInstanceAcquisitionFromBackendFailed,
} from './ta-recognition-rou-rule-update-status.actions';
import { ITAInterestPaidTransferRule } from '../../erp-accounts/ta-interest-paid-transfer-rule/ta-interest-paid-transfer-rule.model';
import { ITALeaseInterestAccrualRule } from '../../erp-accounts/ta-lease-interest-accrual-rule/ta-lease-interest-accrual-rule.model';
import { ITALeaseRecognitionRule } from '../../erp-accounts/ta-lease-recognition-rule/ta-lease-recognition-rule.model';
import { ITALeaseRepaymentRule } from '../../erp-accounts/ta-lease-repayment-rule/ta-lease-repayment-rule.model';
import { ITARecognitionROURule } from '../../erp-accounts/ta-recognition-rou-rule/ta-recognition-rou-rule.model';

describe('TA rule update workflow actions', () => {
  const taInterestPaidTransferRuleSample = { id: 101 } as ITAInterestPaidTransferRule;
  const taLeaseInterestAccrualRuleSample = { id: 102 } as ITALeaseInterestAccrualRule;
  const taLeaseRecognitionRuleSample = { id: 103 } as ITALeaseRecognitionRule;
  const taLeaseRepaymentRuleSample = { id: 104 } as ITALeaseRepaymentRule;
  const taRecognitionRouRuleSample = { id: 105 } as ITARecognitionROURule;

  it('should expose TA Interest Paid Transfer Rule workflow action creators', () => {
    expect(taInterestPaidTransferRuleCreationInitiatedFromList().type).toBe(
      '[TAInterestPaidTransferRule Creation: List] TA Interest Paid Transfer Rule creation workflow initiated'
    );
    expect(taInterestPaidTransferRuleCreationInitiatedEnRoute().type).toBe(
      '[TAInterestPaidTransferRule: Route] TA Interest Paid Transfer Rule create workflow initiated'
    );
    expect(taInterestPaidTransferRuleCreationWorkflowInitiatedFromList().type).toBe(
      '[TAInterestPaidTransferRule Create: List] TA Interest Paid Transfer Rule create workflow initiated'
    );

    expect(
      taInterestPaidTransferRuleCopyWorkflowInitiatedFromList({ copiedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Copy: List] TA Interest Paid Transfer Rule copy workflow initiated',
        copiedInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(
      taInterestPaidTransferRuleCopyWorkflowInitiatedFromView({ copiedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Copy: View] TA Interest Paid Transfer Rule copy workflow initiated',
        copiedInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(
      taInterestPaidTransferRuleCopyWorkflowInitiatedEnRoute({ copiedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Copy: Route] TA Interest Paid Transfer Rule copy workflow initiated',
        copiedInstance: taInterestPaidTransferRuleSample,
      })
    );

    expect(
      taInterestPaidTransferRuleEditWorkflowInitiatedFromList({ editedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Edit: List] TA Interest Paid Transfer Rule edit workflow initiated',
        editedInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(
      taInterestPaidTransferRuleEditWorkflowInitiatedFromView({ editedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Edit: View] TA Interest Paid Transfer Rule edit workflow initiated',
        editedInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(
      taInterestPaidTransferRuleEditWorkflowInitiatedEnRoute({ editedInstance: taInterestPaidTransferRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRule Edit: Route] TA Interest Paid Transfer Rule edit workflow initiated',
        editedInstance: taInterestPaidTransferRuleSample,
      })
    );

    expect(taInterestPaidTransferRuleDataHasMutated().type).toBe(
      '[TAInterestPaidTransferRule Form] TA Interest Paid Transfer Rule form data mutated'
    );
    expect(taInterestPaidTransferRuleUpdateFormHasBeenDestroyed().type).toBe(
      '[TAInterestPaidTransferRule Form] TA Interest Paid Transfer Rule form destroyed'
    );

    expect(
      taInterestPaidTransferRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taInterestPaidTransferRuleSample,
      })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRuleEffects: Copied-TA-Interest-Paid-Transfer-effects] TA Interest Paid Transfer Rule instance acquired for copy',
        backendAcquiredInstance: taInterestPaidTransferRuleSample,
      })
    );
    expect(
      taInterestPaidTransferRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'failed to load' })
    ).toEqual(
      expect.objectContaining({
        type: '[TAInterestPaidTransferRuleEffects: Copied-TA-Interest-Paid-Transfer-effects] TA Interest Paid Transfer Rule instance acquisition failed',
        error: 'failed to load',
      })
    );
  });

  it('should expose TA Lease Interest Accrual Rule workflow action creators', () => {
    expect(taLeaseInterestAccrualRuleCreationInitiatedFromList().type).toBe(
      '[TALeaseInterestAccrualRule Creation: List] TA Lease Interest Accrual Rule creation workflow initiated'
    );
    expect(taLeaseInterestAccrualRuleCreationInitiatedEnRoute().type).toBe(
      '[TALeaseInterestAccrualRule: Route] TA Lease Interest Accrual Rule create workflow initiated'
    );
    expect(taLeaseInterestAccrualRuleCreationWorkflowInitiatedFromList().type).toBe(
      '[TALeaseInterestAccrualRule Create: List] TA Lease Interest Accrual Rule create workflow initiated'
    );

    expect(
      taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Copy: List] TA Lease Interest Accrual Rule copy workflow initiated',
        copiedInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(
      taLeaseInterestAccrualRuleCopyWorkflowInitiatedFromView({ copiedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Copy: View] TA Lease Interest Accrual Rule copy workflow initiated',
        copiedInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(
      taLeaseInterestAccrualRuleCopyWorkflowInitiatedEnRoute({ copiedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Copy: Route] TA Lease Interest Accrual Rule copy workflow initiated',
        copiedInstance: taLeaseInterestAccrualRuleSample,
      })
    );

    expect(
      taLeaseInterestAccrualRuleEditWorkflowInitiatedFromList({ editedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Edit: List] TA Lease Interest Accrual Rule edit workflow initiated',
        editedInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(
      taLeaseInterestAccrualRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Edit: View] TA Lease Interest Accrual Rule edit workflow initiated',
        editedInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(
      taLeaseInterestAccrualRuleEditWorkflowInitiatedEnRoute({ editedInstance: taLeaseInterestAccrualRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRule Edit: Route] TA Lease Interest Accrual Rule edit workflow initiated',
        editedInstance: taLeaseInterestAccrualRuleSample,
      })
    );

    expect(taLeaseInterestAccrualRuleDataHasMutated().type).toBe(
      '[TALeaseInterestAccrualRule Form] TA Lease Interest Accrual Rule form data mutated'
    );
    expect(taLeaseInterestAccrualRuleUpdateFormHasBeenDestroyed().type).toBe(
      '[TALeaseInterestAccrualRule Form] TA Lease Interest Accrual Rule form destroyed'
    );

    expect(
      taLeaseInterestAccrualRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseInterestAccrualRuleSample,
      })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRuleEffects: Copied-TA-Lease-Interest-Accrual-effects] TA Lease Interest Accrual Rule instance acquired for copy',
        backendAcquiredInstance: taLeaseInterestAccrualRuleSample,
      })
    );
    expect(
      taLeaseInterestAccrualRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'failed to load accrual' })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseInterestAccrualRuleEffects: Copied-TA-Lease-Interest-Accrual-effects] TA Lease Interest Accrual Rule instance acquisition failed',
        error: 'failed to load accrual',
      })
    );
  });

  it('should expose TA Lease Recognition Rule workflow action creators', () => {
    expect(taLeaseRecognitionRuleCreationInitiatedFromList().type).toBe(
      '[TALeaseRecognitionRule Creation: List] TA Lease Recognition Rule creation workflow initiated'
    );
    expect(taLeaseRecognitionRuleCreationInitiatedEnRoute().type).toBe(
      '[TALeaseRecognitionRule: Route] TA Lease Recognition Rule create workflow initiated'
    );
    expect(taLeaseRecognitionRuleCreationWorkflowInitiatedFromList().type).toBe(
      '[TALeaseRecognitionRule Create: List] TA Lease Recognition Rule create workflow initiated'
    );

    expect(
      taLeaseRecognitionRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Copy: List] TA Lease Recognition Rule copy workflow initiated',
        copiedInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(
      taLeaseRecognitionRuleCopyWorkflowInitiatedFromView({ copiedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Copy: View] TA Lease Recognition Rule copy workflow initiated',
        copiedInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(
      taLeaseRecognitionRuleCopyWorkflowInitiatedEnRoute({ copiedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Copy: Route] TA Lease Recognition Rule copy workflow initiated',
        copiedInstance: taLeaseRecognitionRuleSample,
      })
    );

    expect(
      taLeaseRecognitionRuleEditWorkflowInitiatedFromList({ editedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Edit: List] TA Lease Recognition Rule edit workflow initiated',
        editedInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(
      taLeaseRecognitionRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Edit: View] TA Lease Recognition Rule edit workflow initiated',
        editedInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(
      taLeaseRecognitionRuleEditWorkflowInitiatedEnRoute({ editedInstance: taLeaseRecognitionRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRule Edit: Route] TA Lease Recognition Rule edit workflow initiated',
        editedInstance: taLeaseRecognitionRuleSample,
      })
    );

    expect(taLeaseRecognitionRuleDataHasMutated().type).toBe(
      '[TALeaseRecognitionRule Form] TA Lease Recognition Rule form data mutated'
    );
    expect(taLeaseRecognitionRuleUpdateFormHasBeenDestroyed().type).toBe(
      '[TALeaseRecognitionRule Form] TA Lease Recognition Rule form destroyed'
    );

    expect(
      taLeaseRecognitionRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseRecognitionRuleSample,
      })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRuleEffects: Copied-TA-Lease-Recognition-effects] TA Lease Recognition Rule instance acquired for copy',
        backendAcquiredInstance: taLeaseRecognitionRuleSample,
      })
    );
    expect(
      taLeaseRecognitionRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'failed to load recognition' })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRecognitionRuleEffects: Copied-TA-Lease-Recognition-effects] TA Lease Recognition Rule instance acquisition failed',
        error: 'failed to load recognition',
      })
    );
  });

  it('should expose TA Lease Repayment Rule workflow action creators', () => {
    expect(taLeaseRepaymentRuleCreationInitiatedFromList().type).toBe(
      '[TALeaseRepaymentRule Creation: List] TA Lease Repayment Rule creation workflow initiated'
    );
    expect(taLeaseRepaymentRuleCreationInitiatedEnRoute().type).toBe(
      '[TALeaseRepaymentRule: Route] TA Lease Repayment Rule create workflow initiated'
    );
    expect(taLeaseRepaymentRuleCreationWorkflowInitiatedFromList().type).toBe(
      '[TALeaseRepaymentRule Create: List] TA Lease Repayment Rule create workflow initiated'
    );

    expect(
      taLeaseRepaymentRuleCopyWorkflowInitiatedFromList({ copiedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Copy: List] TA Lease Repayment Rule copy workflow initiated',
        copiedInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(
      taLeaseRepaymentRuleCopyWorkflowInitiatedFromView({ copiedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Copy: View] TA Lease Repayment Rule copy workflow initiated',
        copiedInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(
      taLeaseRepaymentRuleCopyWorkflowInitiatedEnRoute({ copiedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Copy: Route] TA Lease Repayment Rule copy workflow initiated',
        copiedInstance: taLeaseRepaymentRuleSample,
      })
    );

    expect(
      taLeaseRepaymentRuleEditWorkflowInitiatedFromList({ editedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Edit: List] TA Lease Repayment Rule edit workflow initiated',
        editedInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(
      taLeaseRepaymentRuleEditWorkflowInitiatedFromView({ editedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Edit: View] TA Lease Repayment Rule edit workflow initiated',
        editedInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(
      taLeaseRepaymentRuleEditWorkflowInitiatedEnRoute({ editedInstance: taLeaseRepaymentRuleSample })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRule Edit: Route] TA Lease Repayment Rule edit workflow initiated',
        editedInstance: taLeaseRepaymentRuleSample,
      })
    );

    expect(taLeaseRepaymentRuleDataHasMutated().type).toBe(
      '[TALeaseRepaymentRule Form] TA Lease Repayment Rule form data mutated'
    );
    expect(taLeaseRepaymentRuleUpdateFormHasBeenDestroyed().type).toBe(
      '[TALeaseRepaymentRule Form] TA Lease Repayment Rule form destroyed'
    );

    expect(
      taLeaseRepaymentRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taLeaseRepaymentRuleSample,
      })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRuleEffects: Copied-TA-Lease-Repayment-effects] TA Lease Repayment Rule instance acquired for copy',
        backendAcquiredInstance: taLeaseRepaymentRuleSample,
      })
    );
    expect(
      taLeaseRepaymentRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'failed to load repayment' })
    ).toEqual(
      expect.objectContaining({
        type: '[TALeaseRepaymentRuleEffects: Copied-TA-Lease-Repayment-effects] TA Lease Repayment Rule instance acquisition failed',
        error: 'failed to load repayment',
      })
    );
  });

  it('should expose TA Recognition ROU Rule workflow action creators', () => {
    expect(taRecognitionRouRuleCreationInitiatedFromList().type).toBe(
      '[TARecognitionRouRule Creation: List] TA Recognition Rou Rule creation workflow initiated'
    );
    expect(taRecognitionRouRuleCreationInitiatedEnRoute().type).toBe(
      '[TARecognitionRouRule: Route] TA Recognition Rou Rule create workflow initiated'
    );
    expect(taRecognitionRouRuleCreationWorkflowInitiatedFromList().type).toBe(
      '[TARecognitionRouRule Create: List] TA Recognition Rou Rule create workflow initiated'
    );

    expect(taRecognitionRouRuleCopyWorkflowInitiatedFromList({ copiedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Copy: List] TA Recognition Rou Rule copy workflow initiated',
        copiedInstance: taRecognitionRouRuleSample,
      })
    );
    expect(taRecognitionRouRuleCopyWorkflowInitiatedFromView({ copiedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Copy: View] TA Recognition Rou Rule copy workflow initiated',
        copiedInstance: taRecognitionRouRuleSample,
      })
    );
    expect(taRecognitionRouRuleCopyWorkflowInitiatedEnRoute({ copiedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Copy: Route] TA Recognition Rou Rule copy workflow initiated',
        copiedInstance: taRecognitionRouRuleSample,
      })
    );

    expect(taRecognitionRouRuleEditWorkflowInitiatedFromList({ editedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Edit: List] TA Recognition Rou Rule edit workflow initiated',
        editedInstance: taRecognitionRouRuleSample,
      })
    );
    expect(taRecognitionRouRuleEditWorkflowInitiatedFromView({ editedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Edit: View] TA Recognition Rou Rule edit workflow initiated',
        editedInstance: taRecognitionRouRuleSample,
      })
    );
    expect(taRecognitionRouRuleEditWorkflowInitiatedEnRoute({ editedInstance: taRecognitionRouRuleSample })).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRule Edit: Route] TA Recognition Rou Rule edit workflow initiated',
        editedInstance: taRecognitionRouRuleSample,
      })
    );

    expect(taRecognitionRouRuleDataHasMutated().type).toBe(
      '[TARecognitionRouRule Form] TA Recognition Rou Rule form data mutated'
    );
    expect(taRecognitionRouRuleUpdateFormHasBeenDestroyed().type).toBe(
      '[TARecognitionRouRule Form] TA Recognition Rou Rule form destroyed'
    );

    expect(
      taRecognitionRouRuleUpdateInstanceAcquiredFromBackend({
        backendAcquiredInstance: taRecognitionRouRuleSample,
      })
    ).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRuleEffects: Copied-TA-Recognition-Rou-effects] TA Recognition Rou Rule instance acquired for copy',
        backendAcquiredInstance: taRecognitionRouRuleSample,
      })
    );
    expect(
      taRecognitionRouRuleUpdateInstanceAcquisitionFromBackendFailed({ error: 'failed to load rou rule' })
    ).toEqual(
      expect.objectContaining({
        type: '[TARecognitionRouRuleEffects: Copied-TA-Recognition-Rou-effects] TA Recognition Rou Rule instance acquisition failed',
        error: 'failed to load rou rule',
      })
    );
  });
});
