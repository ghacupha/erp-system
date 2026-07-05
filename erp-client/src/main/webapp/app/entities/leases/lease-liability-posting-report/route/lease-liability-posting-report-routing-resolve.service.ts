import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiabilityPostingReport, LeaseLiabilityPostingReport } from '../lease-liability-posting-report.model';
import { LeaseLiabilityPostingReportService } from '../service/lease-liability-posting-report.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityPostingReportRoutingResolveService implements Resolve<ILeaseLiabilityPostingReport> {
  constructor(protected service: LeaseLiabilityPostingReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiabilityPostingReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiabilityPostingReport: HttpResponse<LeaseLiabilityPostingReport>) => {
          if (leaseLiabilityPostingReport.body) {
            return of(leaseLiabilityPostingReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiabilityPostingReport());
  }
}
