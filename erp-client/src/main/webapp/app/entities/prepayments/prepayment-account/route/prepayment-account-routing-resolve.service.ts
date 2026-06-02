import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentAccount, PrepaymentAccount } from '../prepayment-account.model';
import { PrepaymentAccountService } from '../service/prepayment-account.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentAccountRoutingResolveService implements Resolve<IPrepaymentAccount> {
  constructor(protected service: PrepaymentAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentAccount: HttpResponse<PrepaymentAccount>) => {
          if (prepaymentAccount.body) {
            return of(prepaymentAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentAccount());
  }
}
