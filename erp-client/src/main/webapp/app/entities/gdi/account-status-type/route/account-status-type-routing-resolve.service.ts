import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountStatusType, AccountStatusType } from '../account-status-type.model';
import { AccountStatusTypeService } from '../service/account-status-type.service';

@Injectable({ providedIn: 'root' })
export class AccountStatusTypeRoutingResolveService implements Resolve<IAccountStatusType> {
  constructor(protected service: AccountStatusTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountStatusType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountStatusType: HttpResponse<AccountStatusType>) => {
          if (accountStatusType.body) {
            return of(accountStatusType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountStatusType());
  }
}
