import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';

@Injectable({ providedIn: 'root' })
export class ExchangeRateRoutingResolveService implements Resolve<IExchangeRate> {
  constructor(protected service: ExchangeRateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExchangeRate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exchangeRate: HttpResponse<ExchangeRate>) => {
          if (exchangeRate.body) {
            return of(exchangeRate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExchangeRate());
  }
}
