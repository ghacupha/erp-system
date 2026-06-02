import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityReport, LeaseLiabilityReport } from '../lease-liability-report.model';
import { LeaseLiabilityReportService } from '../service/lease-liability-report.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityReportRoutingResolveService implements Resolve<ILeaseLiabilityReport> {
  constructor(protected service: LeaseLiabilityReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityReport: HttpResponse<LeaseLiabilityReport>) => {
          if (leaseLiabilityReport.body) {
            return of(leaseLiabilityReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityReport());
  }
}
