import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbAccountStatus, CrbAccountStatus } from '../crb-account-status.model';
import { CrbAccountStatusService } from '../service/crb-account-status.service';

@Injectable({ providedIn: 'root' })
export class CrbAccountStatusRoutingResolveService implements Resolve<ICrbAccountStatus> {
  constructor(protected service: CrbAccountStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbAccountStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbAccountStatus: HttpResponse<CrbAccountStatus>) => {
          if (crbAccountStatus.body) {
            return of(crbAccountStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbAccountStatus());
  }
}
