import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiscalQuarter, FiscalQuarter } from '../fiscal-quarter.model';
import { FiscalQuarterService } from '../service/fiscal-quarter.service';

@Injectable({ providedIn: 'root' })
export class FiscalQuarterRoutingResolveService implements Resolve<IFiscalQuarter> {
  constructor(protected service: FiscalQuarterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiscalQuarter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fiscalQuarter: HttpResponse<FiscalQuarter>) => {
          if (fiscalQuarter.body) {
            return of(fiscalQuarter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FiscalQuarter());
  }
}
