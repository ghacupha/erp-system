import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseLiability, LeaseLiability } from '../lease-liability.model';
import { LeaseLiabilityService } from '../service/lease-liability.service';

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityRoutingResolveService implements Resolve<ILeaseLiability> {
  constructor(protected service: LeaseLiabilityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseLiability> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseLiability: HttpResponse<LeaseLiability>) => {
          if (leaseLiability.body) {
            return of(leaseLiability.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseLiability());
  }
}
