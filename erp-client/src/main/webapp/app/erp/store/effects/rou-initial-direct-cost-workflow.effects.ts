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
  rouInitialDirectCostCopyWorkflowInitiatedFromList,
  rouInitialDirectCostUpdateInstanceAcquiredFromBackend,
  rouInitialDirectCostUpdateInstanceAcquisitionFromBackendFailed
} from '../actions/rou-initial-direct-cost-update-status.actions';
import { RouInitialDirectCostService } from '../../erp-leases/rou-initial-direct-cost/service/rou-initial-direct-cost.service';

@Injectable()
export class RouInitialDirectCostWorkflowEffects {

  copiedRouInitialDirectCostWorkflowEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(rouInitialDirectCostCopyWorkflowInitiatedFromList),
      switchMap(action => {
          if (action.copiedInstance.id) {
            return this.rouInitialDirectCostService.find(action.copiedInstance.id).pipe(
              map(backendResponse => rouInitialDirectCostUpdateInstanceAcquiredFromBackend({ backendAcquiredInstance: backendResponse.body ?? action.copiedInstance })),
              catchError(err => of(rouInitialDirectCostUpdateInstanceAcquisitionFromBackendFailed({ error: err })))
            );
          } else {
            return of(action);
          }
        }
      )
    )
  );

  constructor(
    protected actions$: Actions,
    protected rouInitialDirectCostService: RouInitialDirectCostService) {
  }
}
