import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerType, CustomerType } from '../customer-type.model';
import { CustomerTypeService } from '../service/customer-type.service';

@Injectable({ providedIn: 'root' })
export class CustomerTypeRoutingResolveService implements Resolve<ICustomerType> {
  constructor(protected service: CustomerTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerType: HttpResponse<CustomerType>) => {
          if (customerType.body) {
            return of(customerType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomerType());
  }
}
