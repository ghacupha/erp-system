import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerformanceOfForeignSubsidiaries, PerformanceOfForeignSubsidiaries } from '../performance-of-foreign-subsidiaries.model';
import { PerformanceOfForeignSubsidiariesService } from '../service/performance-of-foreign-subsidiaries.service';

@Injectable({ providedIn: 'root' })
export class PerformanceOfForeignSubsidiariesRoutingResolveService implements Resolve<IPerformanceOfForeignSubsidiaries> {
  constructor(protected service: PerformanceOfForeignSubsidiariesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPerformanceOfForeignSubsidiaries> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((performanceOfForeignSubsidiaries: HttpResponse<PerformanceOfForeignSubsidiaries>) => {
          if (performanceOfForeignSubsidiaries.body) {
            return of(performanceOfForeignSubsidiaries.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PerformanceOfForeignSubsidiaries());
  }
}
