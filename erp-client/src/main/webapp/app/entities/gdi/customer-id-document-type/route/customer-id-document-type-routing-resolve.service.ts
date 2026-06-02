import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerIDDocumentType, CustomerIDDocumentType } from '../customer-id-document-type.model';
import { CustomerIDDocumentTypeService } from '../service/customer-id-document-type.service';

@Injectable({ providedIn: 'root' })
export class CustomerIDDocumentTypeRoutingResolveService implements Resolve<ICustomerIDDocumentType> {
  constructor(protected service: CustomerIDDocumentTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerIDDocumentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerIDDocumentType: HttpResponse<CustomerIDDocumentType>) => {
          if (customerIDDocumentType.body) {
            return of(customerIDDocumentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomerIDDocumentType());
  }
}
