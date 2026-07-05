import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIFRS16LeaseContract, IFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';

@Injectable({ providedIn: 'root' })
export class IFRS16LeaseContractRoutingResolveService implements Resolve<IIFRS16LeaseContract> {
  constructor(protected service: IFRS16LeaseContractService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIFRS16LeaseContract> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((iFRS16LeaseContract: HttpResponse<IFRS16LeaseContract>) => {
          if (iFRS16LeaseContract.body) {
            return of(iFRS16LeaseContract.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IFRS16LeaseContract());
  }
}
