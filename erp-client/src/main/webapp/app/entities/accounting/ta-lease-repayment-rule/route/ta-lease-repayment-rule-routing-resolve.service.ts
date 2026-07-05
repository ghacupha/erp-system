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
