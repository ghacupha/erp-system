import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICurrencyAuthenticityFlag, CurrencyAuthenticityFlag } from '../currency-authenticity-flag.model';
import { CurrencyAuthenticityFlagService } from '../service/currency-authenticity-flag.service';

@Injectable({ providedIn: 'root' })
export class CurrencyAuthenticityFlagRoutingResolveService implements Resolve<ICurrencyAuthenticityFlag> {
  constructor(protected service: CurrencyAuthenticityFlagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrencyAuthenticityFlag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((currencyAuthenticityFlag: HttpResponse<CurrencyAuthenticityFlag>) => {
          if (currencyAuthenticityFlag.body) {
            return of(currencyAuthenticityFlag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CurrencyAuthenticityFlag());
  }
}
