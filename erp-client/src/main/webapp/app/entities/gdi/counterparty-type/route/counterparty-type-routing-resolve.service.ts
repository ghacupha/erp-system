import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounterpartyType, CounterpartyType } from '../counterparty-type.model';
import { CounterpartyTypeService } from '../service/counterparty-type.service';

@Injectable({ providedIn: 'root' })
export class CounterpartyTypeRoutingResolveService implements Resolve<ICounterpartyType> {
  constructor(protected service: CounterpartyTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounterpartyType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((counterpartyType: HttpResponse<CounterpartyType>) => {
          if (counterpartyType.body) {
            return of(counterpartyType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CounterpartyType());
  }
}
