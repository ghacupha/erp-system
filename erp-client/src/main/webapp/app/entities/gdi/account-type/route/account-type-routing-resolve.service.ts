import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountType, AccountType } from '../account-type.model';
import { AccountTypeService } from '../service/account-type.service';

@Injectable({ providedIn: 'root' })
export class AccountTypeRoutingResolveService implements Resolve<IAccountType> {
  constructor(protected service: AccountTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountType: HttpResponse<AccountType>) => {
          if (accountType.body) {
            return of(accountType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountType());
  }
}
