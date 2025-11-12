///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
