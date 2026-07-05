import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationRequest, RouDepreciationRequest } from '../rou-depreciation-request.model';
import { RouDepreciationRequestService } from '../service/rou-depreciation-request.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationRequestRoutingResolveService implements Resolve<IRouDepreciationRequest> {
  constructor(protected service: RouDepreciationRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationRequest: HttpResponse<RouDepreciationRequest>) => {
          if (rouDepreciationRequest.body) {
            return of(rouDepreciationRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationRequest());
  }
}
