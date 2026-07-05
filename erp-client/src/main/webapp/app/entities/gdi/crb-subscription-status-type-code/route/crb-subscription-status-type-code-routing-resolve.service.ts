import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbSubscriptionStatusTypeCode, CrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';
import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';

@Injectable({ providedIn: 'root' })
export class CrbSubscriptionStatusTypeCodeRoutingResolveService implements Resolve<ICrbSubscriptionStatusTypeCode> {
  constructor(protected service: CrbSubscriptionStatusTypeCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbSubscriptionStatusTypeCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbSubscriptionStatusTypeCode: HttpResponse<CrbSubscriptionStatusTypeCode>) => {
          if (crbSubscriptionStatusTypeCode.body) {
            return of(crbSubscriptionStatusTypeCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbSubscriptionStatusTypeCode());
  }
}
