import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICollateralInformation, CollateralInformation } from '../collateral-information.model';
import { CollateralInformationService } from '../service/collateral-information.service';

@Injectable({ providedIn: 'root' })
export class CollateralInformationRoutingResolveService implements Resolve<ICollateralInformation> {
  constructor(protected service: CollateralInformationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollateralInformation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((collateralInformation: HttpResponse<CollateralInformation>) => {
          if (collateralInformation.body) {
            return of(collateralInformation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CollateralInformation());
  }
}
