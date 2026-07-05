import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISettlementRequisition, SettlementRequisition } from '../settlement-requisition.model';
import { SettlementRequisitionService } from '../service/settlement-requisition.service';

@Injectable({ providedIn: 'root' })
export class SettlementRequisitionRoutingResolveService implements Resolve<ISettlementRequisition> {
  constructor(protected service: SettlementRequisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISettlementRequisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((settlementRequisition: HttpResponse<SettlementRequisition>) => {
          if (settlementRequisition.body) {
            return of(settlementRequisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SettlementRequisition());
  }
}
