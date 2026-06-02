import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationPeriod, DepreciationPeriod } from '../depreciation-period.model';
import { DepreciationPeriodService } from '../service/depreciation-period.service';

@Injectable({ providedIn: 'root' })
export class DepreciationPeriodRoutingResolveService implements Resolve<IDepreciationPeriod> {
  constructor(protected service: DepreciationPeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationPeriod: HttpResponse<DepreciationPeriod>) => {
          if (depreciationPeriod.body) {
            return of(depreciationPeriod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationPeriod());
  }
}
