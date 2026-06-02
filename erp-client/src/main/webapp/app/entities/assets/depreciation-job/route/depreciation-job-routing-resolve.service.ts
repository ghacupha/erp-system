import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationJob, DepreciationJob } from '../depreciation-job.model';
import { DepreciationJobService } from '../service/depreciation-job.service';

@Injectable({ providedIn: 'root' })
export class DepreciationJobRoutingResolveService implements Resolve<IDepreciationJob> {
  constructor(protected service: DepreciationJobService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationJob> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationJob: HttpResponse<DepreciationJob>) => {
          if (depreciationJob.body) {
            return of(depreciationJob.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationJob());
  }
}
