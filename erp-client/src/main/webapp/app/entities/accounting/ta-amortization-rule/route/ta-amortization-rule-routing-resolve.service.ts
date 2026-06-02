import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITAAmortizationRule, TAAmortizationRule } from '../ta-amortization-rule.model';
import { TAAmortizationRuleService } from '../service/ta-amortization-rule.service';

@Injectable({ providedIn: 'root' })
export class TAAmortizationRuleRoutingResolveService implements Resolve<ITAAmortizationRule> {
  constructor(protected service: TAAmortizationRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITAAmortizationRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tAAmortizationRule: HttpResponse<TAAmortizationRule>) => {
          if (tAAmortizationRule.body) {
            return of(tAAmortizationRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TAAmortizationRule());
  }
}
