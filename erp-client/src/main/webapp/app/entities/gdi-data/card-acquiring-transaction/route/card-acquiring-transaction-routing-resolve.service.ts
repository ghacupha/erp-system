import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICardAcquiringTransaction, CardAcquiringTransaction } from '../card-acquiring-transaction.model';
import { CardAcquiringTransactionService } from '../service/card-acquiring-transaction.service';

@Injectable({ providedIn: 'root' })
export class CardAcquiringTransactionRoutingResolveService implements Resolve<ICardAcquiringTransaction> {
  constructor(protected service: CardAcquiringTransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICardAcquiringTransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cardAcquiringTransaction: HttpResponse<CardAcquiringTransaction>) => {
          if (cardAcquiringTransaction.body) {
            return of(cardAcquiringTransaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CardAcquiringTransaction());
  }
}
