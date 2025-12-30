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

import { createFeatureSelector, createSelector } from '@ngrx/store';
import { State } from '../global-store.definition';
import { leaseTemplateUpdateFormStateSelector } from '../reducers/lease-template-workflow-status.reducer';

export const leaseTemplateUpdateFormState = createFeatureSelector<State>(leaseTemplateUpdateFormStateSelector);

export const leaseTemplateUpdateSelectedInstance = createSelector(
  leaseTemplateUpdateFormState,
  state => state.leaseTemplateFormState.selectedInstance
);

export const editingLeaseTemplateStatus = createSelector(
  leaseTemplateUpdateFormState,
  state => state.leaseTemplateFormState.weAreEditing
);

export const creatingLeaseTemplateStatus = createSelector(
  leaseTemplateUpdateFormState,
  state => state.leaseTemplateFormState.weAreCreating
);

export const copyingLeaseTemplateStatus = createSelector(
  leaseTemplateUpdateFormState,
  state => state.leaseTemplateFormState.weAreCopying
);
