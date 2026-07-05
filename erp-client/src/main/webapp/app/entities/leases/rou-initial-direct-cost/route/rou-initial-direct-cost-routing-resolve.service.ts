import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouInitialDirectCost, RouInitialDirectCost } from '../rou-initial-direct-cost.model';
import { RouInitialDirectCostService } from '../service/rou-initial-direct-cost.service';

@Injectable({ providedIn: 'root' })
export class RouInitialDirectCostRoutingResolveService implements Resolve<IRouInitialDirectCost> {
  constructor(protected service: RouInitialDirectCostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouInitialDirectCost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouInitialDirectCost: HttpResponse<RouInitialDirectCost>) => {
          if (rouInitialDirectCost.body) {
            return of(rouInitialDirectCost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouInitialDirectCost());
  }
}
