import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFxTransactionType, FxTransactionType } from '../fx-transaction-type.model';
import { FxTransactionTypeService } from '../service/fx-transaction-type.service';

@Injectable({ providedIn: 'root' })
export class FxTransactionTypeRoutingResolveService implements Resolve<IFxTransactionType> {
  constructor(protected service: FxTransactionTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFxTransactionType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fxTransactionType: HttpResponse<FxTransactionType>) => {
          if (fxTransactionType.body) {
            return of(fxTransactionType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FxTransactionType());
  }
}
