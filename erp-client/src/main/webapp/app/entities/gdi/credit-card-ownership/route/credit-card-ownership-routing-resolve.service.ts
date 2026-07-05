import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICreditCardOwnership, CreditCardOwnership } from '../credit-card-ownership.model';
import { CreditCardOwnershipService } from '../service/credit-card-ownership.service';

@Injectable({ providedIn: 'root' })
export class CreditCardOwnershipRoutingResolveService implements Resolve<ICreditCardOwnership> {
  constructor(protected service: CreditCardOwnershipService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICreditCardOwnership> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((creditCardOwnership: HttpResponse<CreditCardOwnership>) => {
          if (creditCardOwnership.body) {
            return of(creditCardOwnership.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CreditCardOwnership());
  }
}
