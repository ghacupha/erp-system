import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWIPTransferListReport, WIPTransferListReport } from '../wip-transfer-list-report.model';
import { WIPTransferListReportService } from '../service/wip-transfer-list-report.service';

@Injectable({ providedIn: 'root' })
export class WIPTransferListReportRoutingResolveService implements Resolve<IWIPTransferListReport> {
  constructor(protected service: WIPTransferListReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWIPTransferListReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wIPTransferListReport: HttpResponse<WIPTransferListReport>) => {
          if (wIPTransferListReport.body) {
            return of(wIPTransferListReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WIPTransferListReport());
  }
}
