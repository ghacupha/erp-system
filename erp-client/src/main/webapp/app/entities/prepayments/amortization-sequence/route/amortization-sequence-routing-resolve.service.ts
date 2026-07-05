import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmortizationSequence, AmortizationSequence } from '../amortization-sequence.model';
import { AmortizationSequenceService } from '../service/amortization-sequence.service';

@Injectable({ providedIn: 'root' })
export class AmortizationSequenceRoutingResolveService implements Resolve<IAmortizationSequence> {
  constructor(protected service: AmortizationSequenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmortizationSequence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amortizationSequence: HttpResponse<AmortizationSequence>) => {
          if (amortizationSequence.body) {
            return of(amortizationSequence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AmortizationSequence());
  }
}
