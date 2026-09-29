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

/**
 * Generic "create this related record inline, then come back" workflow, used by any
 * typeahead/picker form-control that offers a "Create New" option (e.g. picking a
 * Settlement on the prepayment-account form, or an Invoice on the settlement form).
 * Not tied to one entity type - entityType/targetField/correlationId are what let several
 * of these be in flight at once (including nested: create Invoice while creating Settlement
 * while editing a PrepaymentAccount) without them clobbering each other.
 */

export const inlineCreateRequested = createAction(
  '[Inline Create] requested',
  props<{
    correlationId: string;
    entityType: string;
    targetField: string;
    returnUrl: string;
  }>()
);

export const inlineCreateCompleted = createAction(
  '[Inline Create] completed',
  props<{
    correlationId: string;
    entityType: string;
    targetField: string;
    createdEntity: unknown;
  }>()
);

export const inlineCreateConsumed = createAction('[Inline Create] consumed', props<{ correlationId: string }>());

export const inlineCreateCancelled = createAction('[Inline Create] cancelled', props<{ correlationId: string }>());
