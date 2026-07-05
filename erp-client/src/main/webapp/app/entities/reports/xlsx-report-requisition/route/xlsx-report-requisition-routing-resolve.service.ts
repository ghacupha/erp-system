import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXlsxReportRequisition, XlsxReportRequisition } from '../xlsx-report-requisition.model';
import { XlsxReportRequisitionService } from '../service/xlsx-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class XlsxReportRequisitionRoutingResolveService implements Resolve<IXlsxReportRequisition> {
  constructor(protected service: XlsxReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXlsxReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xlsxReportRequisition: HttpResponse<XlsxReportRequisition>) => {
          if (xlsxReportRequisition.body) {
            return of(xlsxReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XlsxReportRequisition());
  }
}
