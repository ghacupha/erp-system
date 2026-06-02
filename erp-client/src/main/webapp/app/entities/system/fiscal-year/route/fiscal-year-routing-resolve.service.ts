import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiscalYear, FiscalYear } from '../fiscal-year.model';
import { FiscalYearService } from '../service/fiscal-year.service';

@Injectable({ providedIn: 'root' })
export class FiscalYearRoutingResolveService implements Resolve<IFiscalYear> {
  constructor(protected service: FiscalYearService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiscalYear> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fiscalYear: HttpResponse<FiscalYear>) => {
          if (fiscalYear.body) {
            return of(fiscalYear.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FiscalYear());
  }
}
