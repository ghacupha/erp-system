import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFxTransactionRateType, FxTransactionRateType } from '../fx-transaction-rate-type.model';
import { FxTransactionRateTypeService } from '../service/fx-transaction-rate-type.service';

@Injectable({ providedIn: 'root' })
export class FxTransactionRateTypeRoutingResolveService implements Resolve<IFxTransactionRateType> {
  constructor(protected service: FxTransactionRateTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFxTransactionRateType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fxTransactionRateType: HttpResponse<FxTransactionRateType>) => {
          if (fxTransactionRateType.body) {
            return of(fxTransactionRateType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FxTransactionRateType());
  }
}
