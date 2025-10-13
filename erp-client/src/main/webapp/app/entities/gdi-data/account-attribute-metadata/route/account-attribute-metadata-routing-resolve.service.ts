///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { IAccountAttributeMetadata, AccountAttributeMetadata } from '../account-attribute-metadata.model';
import { AccountAttributeMetadataService } from '../service/account-attribute-metadata.service';

@Injectable({ providedIn: 'root' })
export class AccountAttributeMetadataRoutingResolveService implements Resolve<IAccountAttributeMetadata> {
  constructor(protected service: AccountAttributeMetadataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountAttributeMetadata> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountAttributeMetadata: HttpResponse<AccountAttributeMetadata>) => {
          if (accountAttributeMetadata.body) {
            return of(accountAttributeMetadata.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountAttributeMetadata());
  }
}
