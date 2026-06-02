import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWIPListReport, WIPListReport } from '../wip-list-report.model';
import { WIPListReportService } from '../service/wip-list-report.service';

@Injectable({ providedIn: 'root' })
export class WIPListReportRoutingResolveService implements Resolve<IWIPListReport> {
  constructor(protected service: WIPListReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWIPListReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wIPListReport: HttpResponse<WIPListReport>) => {
          if (wIPListReport.body) {
            return of(wIPListReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WIPListReport());
  }
}
