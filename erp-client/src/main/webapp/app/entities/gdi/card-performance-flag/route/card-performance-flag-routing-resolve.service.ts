import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardPerformanceFlag, CardPerformanceFlag } from '../card-performance-flag.model';
import { CardPerformanceFlagService } from '../service/card-performance-flag.service';

@Injectable({ providedIn: 'root' })
export class CardPerformanceFlagRoutingResolveService implements Resolve<ICardPerformanceFlag> {
  constructor(protected service: CardPerformanceFlagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICardPerformanceFlag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cardPerformanceFlag: HttpResponse<CardPerformanceFlag>) => {
          if (cardPerformanceFlag.body) {
            return of(cardPerformanceFlag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CardPerformanceFlag());
  }
}
