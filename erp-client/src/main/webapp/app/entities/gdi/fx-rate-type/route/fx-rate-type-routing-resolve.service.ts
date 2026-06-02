import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFxRateType, FxRateType } from '../fx-rate-type.model';
import { FxRateTypeService } from '../service/fx-rate-type.service';

@Injectable({ providedIn: 'root' })
export class FxRateTypeRoutingResolveService implements Resolve<IFxRateType> {
  constructor(protected service: FxRateTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFxRateType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fxRateType: HttpResponse<FxRateType>) => {
          if (fxRateType.body) {
            return of(fxRateType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FxRateType());
  }
}
