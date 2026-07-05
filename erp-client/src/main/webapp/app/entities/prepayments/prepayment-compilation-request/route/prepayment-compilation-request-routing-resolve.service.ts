import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrepaymentCompilationRequest, PrepaymentCompilationRequest } from '../prepayment-compilation-request.model';
import { PrepaymentCompilationRequestService } from '../service/prepayment-compilation-request.service';

@Injectable({ providedIn: 'root' })
export class PrepaymentCompilationRequestRoutingResolveService implements Resolve<IPrepaymentCompilationRequest> {
  constructor(protected service: PrepaymentCompilationRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrepaymentCompilationRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prepaymentCompilationRequest: HttpResponse<PrepaymentCompilationRequest>) => {
          if (prepaymentCompilationRequest.body) {
            return of(prepaymentCompilationRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrepaymentCompilationRequest());
  }
}
