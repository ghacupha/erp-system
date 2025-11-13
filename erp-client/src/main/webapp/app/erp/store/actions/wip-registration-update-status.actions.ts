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

import { createAction, props } from '@ngrx/store';
import { IWorkInProgressRegistration } from '../../erp-assets/work-in-progress-registration/work-in-progress-registration.model';

export const wipRegistrationCreationInitiatedFromList = createAction(
  '[WorkInProgress Creation: List] WIP registration creation workflow initiated',
);

export const wipRegistrationCopyWorkflowInitiatedEnRoute = createAction(
  '[WorkInProgress Copy: Route] WIP registration copy workflow initiated',
  props<{ copiedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationCopyWorkflowInitiatedFromList = createAction(
  '[WorkInProgress Copy: List] WIP registration copy workflow initiated',
  props<{ copiedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationCopyWorkflowInitiatedFromView = createAction(
  '[WorkInProgress Copy: View] WIP registration copy workflow initiated',
  props<{ copiedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationEditWorkflowInitiatedEnRoute = createAction(
  '[WorkInProgress Edit: Route] WIP registration edit workflow initiated',
  props<{ editedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationEditWorkflowInitiatedFromList = createAction(
  '[WorkInProgress Edit: List] WIP registration edit workflow initiated',
  props<{ editedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationEditWorkflowInitiatedFromView = createAction(
  '[WorkInProgress Edit: View] WIP registration edit workflow initiated',
  props<{ editedInstance: IWorkInProgressRegistration }>()
);

export const wipRegistrationCreationInitiatedEnRoute = createAction(
  '[WorkInProgress: Route] WIP registration create workflow initiated',
);

export const wipRegistrationCreationWorkflowInitiatedFromList = createAction(
  '[WorkInProgress Create: List] WIP registration create workflow initiated',
);

export const wipRegistrationUpdateFormHasBeenDestroyed = createAction(
  '[WorkInProgress Form] WIP registration form destroyed',
);

export const wipRegistrationDataHasMutated = createAction(
  '[WorkInProgress Form] WIP registration form data mutated',
);
