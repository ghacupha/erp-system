import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportContentType, ReportContentType } from '../report-content-type.model';
import { ReportContentTypeService } from '../service/report-content-type.service';

@Injectable({ providedIn: 'root' })
export class ReportContentTypeRoutingResolveService implements Resolve<IReportContentType> {
  constructor(protected service: ReportContentTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportContentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportContentType: HttpResponse<ReportContentType>) => {
          if (reportContentType.body) {
            return of(reportContentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportContentType());
  }
}
