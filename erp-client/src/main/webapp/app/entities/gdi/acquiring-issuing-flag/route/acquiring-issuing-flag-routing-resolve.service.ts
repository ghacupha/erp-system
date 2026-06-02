import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcquiringIssuingFlag, AcquiringIssuingFlag } from '../acquiring-issuing-flag.model';
import { AcquiringIssuingFlagService } from '../service/acquiring-issuing-flag.service';

@Injectable({ providedIn: 'root' })
export class AcquiringIssuingFlagRoutingResolveService implements Resolve<IAcquiringIssuingFlag> {
  constructor(protected service: AcquiringIssuingFlagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAcquiringIssuingFlag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acquiringIssuingFlag: HttpResponse<AcquiringIssuingFlag>) => {
          if (acquiringIssuingFlag.body) {
            return of(acquiringIssuingFlag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AcquiringIssuingFlag());
  }
}
