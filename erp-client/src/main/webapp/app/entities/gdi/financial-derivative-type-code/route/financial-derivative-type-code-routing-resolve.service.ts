import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinancialDerivativeTypeCode, FinancialDerivativeTypeCode } from '../financial-derivative-type-code.model';
import { FinancialDerivativeTypeCodeService } from '../service/financial-derivative-type-code.service';

@Injectable({ providedIn: 'root' })
export class FinancialDerivativeTypeCodeRoutingResolveService implements Resolve<IFinancialDerivativeTypeCode> {
  constructor(protected service: FinancialDerivativeTypeCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFinancialDerivativeTypeCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((financialDerivativeTypeCode: HttpResponse<FinancialDerivativeTypeCode>) => {
          if (financialDerivativeTypeCode.body) {
            return of(financialDerivativeTypeCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FinancialDerivativeTypeCode());
  }
}
