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
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap, tap } from 'rxjs/operators';

import { IWorkInProgressOverview, WorkInProgressOverview } from '../work-in-progress-overview.model';
import { WorkInProgressOverviewService } from '../service/work-in-progress-overview.service';
import { Store } from '@ngrx/store';
import { State } from '../../../store/global-store.definition';
import { wipOverviewResetReportPathAction } from '../../../store/actions/report-navigation-profile-status.actions';

@Injectable({ providedIn: 'root' })
export class WorkInProgressOverviewRoutingResolveService implements Resolve<IWorkInProgressOverview> {
  constructor(
    protected store: Store<State>,
    protected service: WorkInProgressOverviewService,
    protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkInProgressOverview> | Observable<never> {
    const reportDate = route.params['reportDate'];
    if (reportDate) {
      return this.service.findByDate(reportDate).pipe(
        mergeMap((workInProgressOverview: HttpResponse<WorkInProgressOverview>) => {
          if (workInProgressOverview.body) {
            return of(workInProgressOverview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      ).pipe(tap(() => {
        // TODO cleanup report-path which is no longer required
        this.store.dispatch(wipOverviewResetReportPathAction());
      }));
    }
    return of(new WorkInProgressOverview());
  }
}
