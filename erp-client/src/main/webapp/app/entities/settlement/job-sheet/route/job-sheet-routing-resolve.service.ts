import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobSheet, JobSheet } from '../job-sheet.model';
import { JobSheetService } from '../service/job-sheet.service';

@Injectable({ providedIn: 'root' })
export class JobSheetRoutingResolveService implements Resolve<IJobSheet> {
  constructor(protected service: JobSheetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJobSheet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jobSheet: HttpResponse<JobSheet>) => {
          if (jobSheet.body) {
            return of(jobSheet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new JobSheet());
  }
}
