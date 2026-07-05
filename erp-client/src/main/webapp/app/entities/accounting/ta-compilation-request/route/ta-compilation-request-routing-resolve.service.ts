import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITACompilationRequest, TACompilationRequest } from '../ta-compilation-request.model';
import { TACompilationRequestService } from '../service/ta-compilation-request.service';

@Injectable({ providedIn: 'root' })
export class TACompilationRequestRoutingResolveService implements Resolve<ITACompilationRequest> {
  constructor(protected service: TACompilationRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITACompilationRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tACompilationRequest: HttpResponse<TACompilationRequest>) => {
          if (tACompilationRequest.body) {
            return of(tACompilationRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TACompilationRequest());
  }
}
