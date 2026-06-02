import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITAInterestPaidTransferRule, TAInterestPaidTransferRule } from '../ta-interest-paid-transfer-rule.model';
import { TAInterestPaidTransferRuleService } from '../service/ta-interest-paid-transfer-rule.service';

@Injectable({ providedIn: 'root' })
export class TAInterestPaidTransferRuleRoutingResolveService implements Resolve<ITAInterestPaidTransferRule> {
  constructor(protected service: TAInterestPaidTransferRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITAInterestPaidTransferRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tAInterestPaidTransferRule: HttpResponse<TAInterestPaidTransferRule>) => {
          if (tAInterestPaidTransferRule.body) {
            return of(tAInterestPaidTransferRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TAInterestPaidTransferRule());
  }
}
