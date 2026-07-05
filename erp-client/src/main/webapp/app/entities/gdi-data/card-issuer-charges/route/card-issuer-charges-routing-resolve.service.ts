import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardIssuerCharges, CardIssuerCharges } from '../card-issuer-charges.model';
import { CardIssuerChargesService } from '../service/card-issuer-charges.service';

@Injectable({ providedIn: 'root' })
export class CardIssuerChargesRoutingResolveService implements Resolve<ICardIssuerCharges> {
  constructor(protected service: CardIssuerChargesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICardIssuerCharges> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cardIssuerCharges: HttpResponse<CardIssuerCharges>) => {
          if (cardIssuerCharges.body) {
            return of(cardIssuerCharges.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CardIssuerCharges());
  }
}
