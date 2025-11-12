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
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { ISettlement, Settlement } from '../settlement.model';
import { SettlementService } from '../service/settlement.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { settlementCreationWorkflowInitiatedEnRoute } from '../../../store/actions/settlement-update-menu.actions';

@Injectable({ providedIn: 'root' })
export class SettlementNewRoutingResolveService implements Resolve<ISettlement> {

  constructor(
    protected service: SettlementService,
    protected store: Store<State>,
    protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettlement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settlement: HttpResponse<Settlement>) => {
          if (settlement.body) {
            this.store.dispatch(settlementCreationWorkflowInitiatedEnRoute());
            return of(settlement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Settlement());
  }
}
