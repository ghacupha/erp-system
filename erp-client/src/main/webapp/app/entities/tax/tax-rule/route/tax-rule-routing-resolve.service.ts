import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaxRule, TaxRule } from '../tax-rule.model';
import { TaxRuleService } from '../service/tax-rule.service';

@Injectable({ providedIn: 'root' })
export class TaxRuleRoutingResolveService implements Resolve<ITaxRule> {
  constructor(protected service: TaxRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taxRule: HttpResponse<TaxRule>) => {
          if (taxRule.body) {
            return of(taxRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaxRule());
  }
}
