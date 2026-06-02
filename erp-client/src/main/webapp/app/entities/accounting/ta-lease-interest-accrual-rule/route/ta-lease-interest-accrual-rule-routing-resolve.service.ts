import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITALeaseInterestAccrualRule, TALeaseInterestAccrualRule } from '../ta-lease-interest-accrual-rule.model';
import { TALeaseInterestAccrualRuleService } from '../service/ta-lease-interest-accrual-rule.service';

@Injectable({ providedIn: 'root' })
export class TALeaseInterestAccrualRuleRoutingResolveService implements Resolve<ITALeaseInterestAccrualRule> {
  constructor(protected service: TALeaseInterestAccrualRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITALeaseInterestAccrualRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tALeaseInterestAccrualRule: HttpResponse<TALeaseInterestAccrualRule>) => {
          if (tALeaseInterestAccrualRule.body) {
            return of(tALeaseInterestAccrualRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TALeaseInterestAccrualRule());
  }
}
