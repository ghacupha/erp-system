import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentMarshalling, PrepaymentMarshalling } from '../prepayment-marshalling.model';
import { PrepaymentMarshallingService } from '../service/prepayment-marshalling.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentMarshallingRoutingResolveService implements Resolve<IPrepaymentMarshalling> {
  constructor(protected service: PrepaymentMarshallingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentMarshalling> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentMarshalling: HttpResponse<PrepaymentMarshalling>) => {
          if (prepaymentMarshalling.body) {
            return of(prepaymentMarshalling.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentMarshalling());
  }
}
