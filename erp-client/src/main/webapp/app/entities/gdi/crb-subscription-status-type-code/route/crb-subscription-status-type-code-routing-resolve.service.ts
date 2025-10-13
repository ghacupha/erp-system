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

import { ICrbSubscriptionStatusTypeCode, CrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';
import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';

@Injectable({ providedIn: 'root' })
export class CrbSubscriptionStatusTypeCodeRoutingResolveService implements Resolve<ICrbSubscriptionStatusTypeCode> {
  constructor(protected service: CrbSubscriptionStatusTypeCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbSubscriptionStatusTypeCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbSubscriptionStatusTypeCode: HttpResponse<CrbSubscriptionStatusTypeCode>) => {
          if (crbSubscriptionStatusTypeCode.body) {
            return of(crbSubscriptionStatusTypeCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbSubscriptionStatusTypeCode());
  }
}
