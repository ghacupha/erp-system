import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISettlement, Settlement } from '../settlement.model';
import { SettlementService } from '../service/settlement.service';

@Injectable({ providedIn: 'root' })
export class SettlementRoutingResolveService implements Resolve<ISettlement> {
  constructor(protected service: SettlementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettlement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settlement: HttpResponse<Settlement>) => {
          if (settlement.body) {
            return of(settlement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Settlement());
  }
}
