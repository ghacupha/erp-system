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
import {
  rouMetadataCopyWorkflowInitiatedFromList,
  rouMetadataUpdateInstanceAcquiredFromBackend,
  rouMetadataUpdateInstanceAcquisitionFromBackendFailed
} from '../actions/rou-model-metadata-update-status.actions';
import { RouModelMetadataService } from '../../erp-leases/rou-model-metadata/service/rou-model-metadata.service';

@Injectable()
export class RouModelMetadataWorkflowEffects {

  copiedRouModelWorkflowEffect$ = createEffect(() =>
    this.actions$.pipe(
      ofType(rouMetadataCopyWorkflowInitiatedFromList),
      // eslint-disable-next-line arrow-body-style
      switchMap(action => {
          if (action.copiedInstance.id) {
            return this.rouModelMetadataService.find(action.copiedInstance.id).pipe(
              map(backendResponse => rouMetadataUpdateInstanceAcquiredFromBackend({ backendAcquiredInstance: backendResponse.body ?? action.copiedInstance })),
              catchError(err => of(rouMetadataUpdateInstanceAcquisitionFromBackendFailed({ error: err })))
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
    protected rouModelMetadataService: RouModelMetadataService) {
  }
}
