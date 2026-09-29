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
import { ISettlement } from '../../erp-settlements/settlement/settlement.model';

/**
 * Dedicated to Settlement specifically - not a generic "inline create" action shared across
 * entity types. The parent form (e.g. PrepaymentAccountUpdateComponent) dispatches
 * settlementInlineCreateStarted itself, right before navigating to the Settlement create route,
 * carrying its OWN full form snapshot (parentFormSnapshot) so it can restore itself completely
 * on return - not just re-fetch/re-render from a bare route and patch one field. The Settlement
 * create form never touches parentFormSnapshot; it only needs targetField (which of its own form
 * fields to end up populating) and parentRoute (where to navigate back to once saved).
 */

export const settlementInlineCreateStarted = createAction(
  '[Settlement Inline Create] started',
  props<{
    targetField: string;
    parentRoute: string;
    parentFormSnapshot: unknown;
  }>()
);

export const settlementInlineCreateCompleted = createAction(
  '[Settlement Inline Create] completed',
  props<{ createdSettlement: ISettlement }>()
);

export const settlementInlineCreateConsumed = createAction('[Settlement Inline Create] consumed');

export const settlementInlineCreateCancelled = createAction('[Settlement Inline Create] cancelled');
