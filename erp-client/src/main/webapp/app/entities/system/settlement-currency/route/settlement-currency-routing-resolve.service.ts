import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISettlementCurrency, SettlementCurrency } from '../settlement-currency.model';
import { SettlementCurrencyService } from '../service/settlement-currency.service';

@Injectable({ providedIn: 'root' })
export class SettlementCurrencyRoutingResolveService implements Resolve<ISettlementCurrency> {
  constructor(protected service: SettlementCurrencyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettlementCurrency> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settlementCurrency: HttpResponse<SettlementCurrency>) => {
          if (settlementCurrency.body) {
            return of(settlementCurrency.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SettlementCurrency());
  }
}
