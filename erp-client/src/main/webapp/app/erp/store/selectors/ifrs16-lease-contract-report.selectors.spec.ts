import { initialState, State } from '../global-store.definition';
import { selectedLeaseContractIdForReport } from './ifrs16-lease-contract-report.selectors';

describe('IFRS16 lease contract report selectors', () => {
  it('should extract the selected lease contract identifier', () => {
    const featureState: State = {
      ...initialState,
      ifrs16LeaseContractReportState: { selectedLeaseContractId: 17 },
    };

    const rootState = { ifrs16LeaseContractReport: featureState } as Record<string, State>;

    expect(selectedLeaseContractIdForReport(rootState)).toBe(17);
  });
});
