///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { PrepaymentMarshallingService } from '../../erp-prepayments/prepayment-marshalling/service/prepayment-marshalling.service';
import {
  prepaymentMarshallingCopyWorkflowInitiatedFromList,
  prepaymentMarshallingUpdateInstanceAcquiredFromBackend,
  prepaymentMarshallingUpdateInstanceAcquisitionFromBackendFailed
} from '../actions/prepayment-marshalling-update-status.actions';

@Injectable()
export class PrepaymentMarshallingWorkflowEffects {

  copiedPrepaymentMarshallingEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(prepaymentMarshallingCopyWorkflowInitiatedFromList),
      // eslint-disable-next-line arrow-body-style
      switchMap(action => {
          if (action.copiedInstance.id) {
            return this.prepaymentMarshallingService.find(action.copiedInstance.id).pipe(
              map(backendResponse => prepaymentMarshallingUpdateInstanceAcquiredFromBackend({ backendAcquiredInstance: backendResponse.body ?? action.copiedInstance })),
              catchError(err => of(prepaymentMarshallingUpdateInstanceAcquisitionFromBackendFailed({ error: err })))
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
    protected prepaymentMarshallingService: PrepaymentMarshallingService) {
  }
}
