import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITaxReference, TaxReference } from '../tax-reference.model';
import { TaxReferenceService } from '../service/tax-reference.service';

@Injectable({ providedIn: 'root' })
export class TaxReferenceRoutingResolveService implements Resolve<ITaxReference> {
  constructor(protected service: TaxReferenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxReference> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((taxReference: HttpResponse<TaxReference>) => {
          if (taxReference.body) {
            return of(taxReference.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaxReference());
  }
}
