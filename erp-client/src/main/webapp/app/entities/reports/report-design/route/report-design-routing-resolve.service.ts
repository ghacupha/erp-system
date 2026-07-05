import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportDesign, ReportDesign } from '../report-design.model';
import { ReportDesignService } from '../service/report-design.service';

@Injectable({ providedIn: 'root' })
export class ReportDesignRoutingResolveService implements Resolve<IReportDesign> {
  constructor(protected service: ReportDesignService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportDesign> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportDesign: HttpResponse<ReportDesign>) => {
          if (reportDesign.body) {
            return of(reportDesign.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportDesign());
  }
}
