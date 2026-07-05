import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardFraudIncidentCategory, CardFraudIncidentCategory } from '../card-fraud-incident-category.model';
import { CardFraudIncidentCategoryService } from '../service/card-fraud-incident-category.service';

@Injectable({ providedIn: 'root' })
export class CardFraudIncidentCategoryRoutingResolveService implements Resolve<ICardFraudIncidentCategory> {
  constructor(protected service: CardFraudIncidentCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICardFraudIncidentCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cardFraudIncidentCategory: HttpResponse<CardFraudIncidentCategory>) => {
          if (cardFraudIncidentCategory.body) {
            return of(cardFraudIncidentCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CardFraudIncidentCategory());
  }
}
