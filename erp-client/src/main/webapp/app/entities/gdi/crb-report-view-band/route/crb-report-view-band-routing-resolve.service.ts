import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbReportViewBand, CrbReportViewBand } from '../crb-report-view-band.model';
import { CrbReportViewBandService } from '../service/crb-report-view-band.service';

@Injectable({ providedIn: 'root' })
export class CrbReportViewBandRoutingResolveService implements Resolve<ICrbReportViewBand> {
  constructor(protected service: CrbReportViewBandService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbReportViewBand> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbReportViewBand: HttpResponse<CrbReportViewBand>) => {
          if (crbReportViewBand.body) {
            return of(crbReportViewBand.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbReportViewBand());
  }
}
