import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICollateralType, CollateralType } from '../collateral-type.model';
import { CollateralTypeService } from '../service/collateral-type.service';

@Injectable({ providedIn: 'root' })
export class CollateralTypeRoutingResolveService implements Resolve<ICollateralType> {
  constructor(protected service: CollateralTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollateralType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((collateralType: HttpResponse<CollateralType>) => {
          if (collateralType.body) {
            return of(collateralType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CollateralType());
  }
}
