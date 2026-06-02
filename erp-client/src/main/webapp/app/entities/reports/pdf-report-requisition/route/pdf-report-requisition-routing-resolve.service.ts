import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPdfReportRequisition, PdfReportRequisition } from '../pdf-report-requisition.model';
import { PdfReportRequisitionService } from '../service/pdf-report-requisition.service';

@Injectable({ providedIn: 'root' })
export class PdfReportRequisitionRoutingResolveService implements Resolve<IPdfReportRequisition> {
  constructor(protected service: PdfReportRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPdfReportRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pdfReportRequisition: HttpResponse<PdfReportRequisition>) => {
          if (pdfReportRequisition.body) {
            return of(pdfReportRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PdfReportRequisition());
  }
}
