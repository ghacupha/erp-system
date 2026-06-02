import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardStatusFlag, CardStatusFlag } from '../card-status-flag.model';
import { CardStatusFlagService } from '../service/card-status-flag.service';

@Injectable({ providedIn: 'root' })
export class CardStatusFlagRoutingResolveService implements Resolve<ICardStatusFlag> {
  constructor(protected service: CardStatusFlagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICardStatusFlag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cardStatusFlag: HttpResponse<CardStatusFlag>) => {
          if (cardStatusFlag.body) {
            return of(cardStatusFlag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CardStatusFlag());
  }
}
