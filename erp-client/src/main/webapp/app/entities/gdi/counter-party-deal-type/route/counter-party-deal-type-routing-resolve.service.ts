import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounterPartyDealType, CounterPartyDealType } from '../counter-party-deal-type.model';
import { CounterPartyDealTypeService } from '../service/counter-party-deal-type.service';

@Injectable({ providedIn: 'root' })
export class CounterPartyDealTypeRoutingResolveService implements Resolve<ICounterPartyDealType> {
  constructor(protected service: CounterPartyDealTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounterPartyDealType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((counterPartyDealType: HttpResponse<CounterPartyDealType>) => {
          if (counterPartyDealType.body) {
            return of(counterPartyDealType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CounterPartyDealType());
  }
}
