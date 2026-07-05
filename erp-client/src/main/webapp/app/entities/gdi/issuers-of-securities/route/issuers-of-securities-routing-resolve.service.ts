import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIssuersOfSecurities, IssuersOfSecurities } from '../issuers-of-securities.model';
import { IssuersOfSecuritiesService } from '../service/issuers-of-securities.service';

@Injectable({ providedIn: 'root' })
export class IssuersOfSecuritiesRoutingResolveService implements Resolve<IIssuersOfSecurities> {
  constructor(protected service: IssuersOfSecuritiesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIssuersOfSecurities> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((issuersOfSecurities: HttpResponse<IssuersOfSecurities>) => {
          if (issuersOfSecurities.body) {
            return of(issuersOfSecurities.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IssuersOfSecurities());
  }
}
