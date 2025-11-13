///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { ITALeaseRepaymentRule, TALeaseRepaymentRule } from '../ta-lease-repayment-rule.model';
import { TALeaseRepaymentRuleService } from '../service/ta-lease-repayment-rule.service';

@Injectable({ providedIn: 'root' })
export class TALeaseRepaymentRuleRoutingResolveService implements Resolve<ITALeaseRepaymentRule> {
  constructor(protected service: TALeaseRepaymentRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITALeaseRepaymentRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tALeaseRepaymentRule: HttpResponse<TALeaseRepaymentRule>) => {
          if (tALeaseRepaymentRule.body) {
            return of(tALeaseRepaymentRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TALeaseRepaymentRule());
  }
}
