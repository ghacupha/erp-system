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

import {
  leaseTemplatePrefillTemplate,
  leaseTemplateSourceLeaseContract,
} from './lease-template-workflows-status.selector';
import { State } from '../global-store.definition';

describe('LeaseTemplate workflow selectors', () => {
  it('should select prefill template', () => {
    const state = {
      leaseTemplateFormState: {
        prefillTemplate: { templateTitle: 'Template-001' },
        sourceLeaseContract: null,
        selectedInstance: {},
        backEndFetchComplete: false,
        browserHasBeenRefreshed: false,
        weAreCopying: false,
        weAreEditing: false,
        weAreCreating: true,
      },
    } as unknown as State;

    expect(leaseTemplatePrefillTemplate(state)).toEqual({ templateTitle: 'Template-001' });
  });

  it('should select source lease contract', () => {
    const state = {
      leaseTemplateFormState: {
        prefillTemplate: {},
        sourceLeaseContract: { id: 11 },
        selectedInstance: {},
        backEndFetchComplete: false,
        browserHasBeenRefreshed: false,
        weAreCopying: false,
        weAreEditing: false,
        weAreCreating: true,
      },
    } as unknown as State;

    expect(leaseTemplateSourceLeaseContract(state)).toEqual({ id: 11 });
  });
});
