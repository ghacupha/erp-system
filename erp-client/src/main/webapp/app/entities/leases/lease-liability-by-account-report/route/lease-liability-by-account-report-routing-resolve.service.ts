import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityByAccountReport, LeaseLiabilityByAccountReport } from '../lease-liability-by-account-report.model';
import { LeaseLiabilityByAccountReportService } from '../service/lease-liability-by-account-report.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityByAccountReportRoutingResolveService implements Resolve<ILeaseLiabilityByAccountReport> {
  constructor(protected service: LeaseLiabilityByAccountReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityByAccountReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityByAccountReport: HttpResponse<LeaseLiabilityByAccountReport>) => {
          if (leaseLiabilityByAccountReport.body) {
            return of(leaseLiabilityByAccountReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityByAccountReport());
  }
}
