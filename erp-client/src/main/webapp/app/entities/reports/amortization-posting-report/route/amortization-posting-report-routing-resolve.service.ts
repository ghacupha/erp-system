import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmortizationPostingReport, AmortizationPostingReport } from '../amortization-posting-report.model';
import { AmortizationPostingReportService } from '../service/amortization-posting-report.service';

@Injectable({ providedIn: 'root' })
export class AmortizationPostingReportRoutingResolveService implements Resolve<IAmortizationPostingReport> {
  constructor(protected service: AmortizationPostingReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmortizationPostingReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amortizationPostingReport: HttpResponse<AmortizationPostingReport>) => {
          if (amortizationPostingReport.body) {
            return of(amortizationPostingReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AmortizationPostingReport());
  }
}
