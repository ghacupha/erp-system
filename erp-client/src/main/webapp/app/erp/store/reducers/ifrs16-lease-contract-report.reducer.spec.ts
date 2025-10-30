import { initialState } from '../global-store.definition';
import {
  ifrs16LeaseContractReportReset,
  ifrs16LeaseContractReportSelected,
} from '../actions/ifrs16-lease-contract-report.actions';
import { ifrs16LeaseContractReportStateReducer } from './ifrs16-lease-contract-report.reducer';

describe('IFRS16 lease contract report reducer', () => {
  it('should record the selected lease contract identifier', () => {
    const result = ifrs16LeaseContractReportStateReducer(
      initialState,
      ifrs16LeaseContractReportSelected({ selectedLeaseContractId: 42 })
    );

    expect(result.ifrs16LeaseContractReportState.selectedLeaseContractId).toBe(42);
  });

  it('should clear the stored lease contract identifier on reset', () => {
    const seededState = {
      ...initialState,
      ifrs16LeaseContractReportState: { selectedLeaseContractId: 99 },
    };

    const result = ifrs16LeaseContractReportStateReducer(seededState, ifrs16LeaseContractReportReset());

    expect(result.ifrs16LeaseContractReportState.selectedLeaseContractId).toBeUndefined();
  });
});
