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

import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import {
  settlementCopyWorkflowInitiatedFromList,
  settlementUpdateInstanceAcquiredFromBackend, settlementUpdateInstanceAcquisitionFromBackendFailed
} from '../actions/settlement-update-menu.actions';
import { SettlementService } from '../../erp-settlements/settlement/service/settlement.service';

@Injectable()
export class SettlementUpdatesEffects {

  copiedSettlementEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(settlementCopyWorkflowInitiatedFromList),
      // eslint-disable-next-line arrow-body-style
      switchMap(action => {
          if (action.copiedSettlement.id) {
            return this.settlementService.find(action.copiedSettlement.id).pipe(
              map(backendResponse => settlementUpdateInstanceAcquiredFromBackend({ backendAcquiredSettlement: backendResponse.body ?? action.copiedSettlement })),
              catchError(err => of(settlementUpdateInstanceAcquisitionFromBackendFailed({ error: err })))
            );
          } else {
            return of(action)
          }
        }
      )
    )
  );

  constructor(
    protected actions$: Actions,
    protected settlementService: SettlementService) {
  }
}
