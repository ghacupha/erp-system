import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbReportRequestReasons, CrbReportRequestReasons } from '../crb-report-request-reasons.model';
import { CrbReportRequestReasonsService } from '../service/crb-report-request-reasons.service';

@Injectable({ providedIn: 'root' })
export class CrbReportRequestReasonsRoutingResolveService implements Resolve<ICrbReportRequestReasons> {
  constructor(protected service: CrbReportRequestReasonsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbReportRequestReasons> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbReportRequestReasons: HttpResponse<CrbReportRequestReasons>) => {
          if (crbReportRequestReasons.body) {
            return of(crbReportRequestReasons.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbReportRequestReasons());
  }
}
