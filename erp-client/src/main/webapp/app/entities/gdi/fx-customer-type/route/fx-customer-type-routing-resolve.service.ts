import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFxCustomerType, FxCustomerType } from '../fx-customer-type.model';
import { FxCustomerTypeService } from '../service/fx-customer-type.service';

@Injectable({ providedIn: 'root' })
export class FxCustomerTypeRoutingResolveService implements Resolve<IFxCustomerType> {
  constructor(protected service: FxCustomerTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFxCustomerType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fxCustomerType: HttpResponse<FxCustomerType>) => {
          if (fxCustomerType.body) {
            return of(fxCustomerType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FxCustomerType());
  }
}
