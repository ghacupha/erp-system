import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INbvCompilationBatch, NbvCompilationBatch } from '../nbv-compilation-batch.model';
import { NbvCompilationBatchService } from '../service/nbv-compilation-batch.service';

@Injectable({ providedIn: 'root' })
export class NbvCompilationBatchRoutingResolveService implements Resolve<INbvCompilationBatch> {
  constructor(protected service: NbvCompilationBatchService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INbvCompilationBatch> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nbvCompilationBatch: HttpResponse<NbvCompilationBatch>) => {
          if (nbvCompilationBatch.body) {
            return of(nbvCompilationBatch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NbvCompilationBatch());
  }
}
