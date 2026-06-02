import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmortizationPeriod, AmortizationPeriod } from '../amortization-period.model';
import { AmortizationPeriodService } from '../service/amortization-period.service';

@Injectable({ providedIn: 'root' })
export class AmortizationPeriodRoutingResolveService implements Resolve<IAmortizationPeriod> {
  constructor(protected service: AmortizationPeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmortizationPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amortizationPeriod: HttpResponse<AmortizationPeriod>) => {
          if (amortizationPeriod.body) {
            return of(amortizationPeriod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AmortizationPeriod());
  }
}
