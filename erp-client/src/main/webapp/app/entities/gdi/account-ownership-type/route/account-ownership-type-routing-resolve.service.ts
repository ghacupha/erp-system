import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountOwnershipType, AccountOwnershipType } from '../account-ownership-type.model';
import { AccountOwnershipTypeService } from '../service/account-ownership-type.service';

@Injectable({ providedIn: 'root' })
export class AccountOwnershipTypeRoutingResolveService implements Resolve<IAccountOwnershipType> {
  constructor(protected service: AccountOwnershipTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountOwnershipType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountOwnershipType: HttpResponse<AccountOwnershipType>) => {
          if (accountOwnershipType.body) {
            return of(accountOwnershipType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountOwnershipType());
  }
}
