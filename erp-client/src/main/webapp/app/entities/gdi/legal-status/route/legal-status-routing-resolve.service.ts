import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILegalStatus, LegalStatus } from '../legal-status.model';
import { LegalStatusService } from '../service/legal-status.service';

@Injectable({ providedIn: 'root' })
export class LegalStatusRoutingResolveService implements Resolve<ILegalStatus> {
  constructor(protected service: LegalStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILegalStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((legalStatus: HttpResponse<LegalStatus>) => {
          if (legalStatus.body) {
            return of(legalStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LegalStatus());
  }
}
