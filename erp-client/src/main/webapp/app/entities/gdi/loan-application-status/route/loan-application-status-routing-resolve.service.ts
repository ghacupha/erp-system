import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoanApplicationStatus, LoanApplicationStatus } from '../loan-application-status.model';
import { LoanApplicationStatusService } from '../service/loan-application-status.service';

@Injectable({ providedIn: 'root' })
export class LoanApplicationStatusRoutingResolveService implements Resolve<ILoanApplicationStatus> {
  constructor(protected service: LoanApplicationStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILoanApplicationStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((loanApplicationStatus: HttpResponse<LoanApplicationStatus>) => {
          if (loanApplicationStatus.body) {
            return of(loanApplicationStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LoanApplicationStatus());
  }
}
