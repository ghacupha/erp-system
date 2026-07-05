import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INbvCompilationJob, NbvCompilationJob } from '../nbv-compilation-job.model';
import { NbvCompilationJobService } from '../service/nbv-compilation-job.service';

@Injectable({ providedIn: 'root' })
export class NbvCompilationJobRoutingResolveService implements Resolve<INbvCompilationJob> {
  constructor(protected service: NbvCompilationJobService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INbvCompilationJob> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nbvCompilationJob: HttpResponse<NbvCompilationJob>) => {
          if (nbvCompilationJob.body) {
            return of(nbvCompilationJob.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NbvCompilationJob());
  }
}
