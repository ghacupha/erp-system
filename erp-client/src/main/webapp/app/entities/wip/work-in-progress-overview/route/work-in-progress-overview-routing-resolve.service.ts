import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkInProgressOverview, WorkInProgressOverview } from '../work-in-progress-overview.model';
import { WorkInProgressOverviewService } from '../service/work-in-progress-overview.service';

@Injectable({ providedIn: 'root' })
export class WorkInProgressOverviewRoutingResolveService implements Resolve<IWorkInProgressOverview> {
  constructor(protected service: WorkInProgressOverviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkInProgressOverview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workInProgressOverview: HttpResponse<WorkInProgressOverview>) => {
          if (workInProgressOverview.body) {
            return of(workInProgressOverview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkInProgressOverview());
  }
}
