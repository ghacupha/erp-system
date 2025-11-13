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
