import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlgorithm, Algorithm } from '../algorithm.model';
import { AlgorithmService } from '../service/algorithm.service';

@Injectable({ providedIn: 'root' })
export class AlgorithmRoutingResolveService implements Resolve<IAlgorithm> {
  constructor(protected service: AlgorithmService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlgorithm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((algorithm: HttpResponse<Algorithm>) => {
          if (algorithm.body) {
            return of(algorithm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Algorithm());
  }
}
