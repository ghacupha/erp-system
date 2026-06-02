import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INbvReport, NbvReport } from '../nbv-report.model';
import { NbvReportService } from '../service/nbv-report.service';

@Injectable({ providedIn: 'root' })
export class NbvReportRoutingResolveService implements Resolve<INbvReport> {
  constructor(protected service: NbvReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INbvReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nbvReport: HttpResponse<NbvReport>) => {
          if (nbvReport.body) {
            return of(nbvReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NbvReport());
  }
}
