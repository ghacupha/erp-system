import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbCustomerType, CrbCustomerType } from '../crb-customer-type.model';
import { CrbCustomerTypeService } from '../service/crb-customer-type.service';

@Injectable({ providedIn: 'root' })
export class CrbCustomerTypeRoutingResolveService implements Resolve<ICrbCustomerType> {
  constructor(protected service: CrbCustomerTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbCustomerType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbCustomerType: HttpResponse<CrbCustomerType>) => {
          if (crbCustomerType.body) {
            return of(crbCustomerType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbCustomerType());
  }
}
