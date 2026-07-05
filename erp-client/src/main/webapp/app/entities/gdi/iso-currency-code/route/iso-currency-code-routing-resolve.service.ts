import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsoCurrencyCode, IsoCurrencyCode } from '../iso-currency-code.model';
import { IsoCurrencyCodeService } from '../service/iso-currency-code.service';

@Injectable({ providedIn: 'root' })
export class IsoCurrencyCodeRoutingResolveService implements Resolve<IIsoCurrencyCode> {
  constructor(protected service: IsoCurrencyCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsoCurrencyCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isoCurrencyCode: HttpResponse<IsoCurrencyCode>) => {
          if (isoCurrencyCode.body) {
            return of(isoCurrencyCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IsoCurrencyCode());
  }
}
