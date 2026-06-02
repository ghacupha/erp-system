import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentMapping, PrepaymentMapping } from '../prepayment-mapping.model';
import { PrepaymentMappingService } from '../service/prepayment-mapping.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentMappingRoutingResolveService implements Resolve<IPrepaymentMapping> {
  constructor(protected service: PrepaymentMappingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentMapping> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentMapping: HttpResponse<PrepaymentMapping>) => {
          if (prepaymentMapping.body) {
            return of(prepaymentMapping.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentMapping());
  }
}
