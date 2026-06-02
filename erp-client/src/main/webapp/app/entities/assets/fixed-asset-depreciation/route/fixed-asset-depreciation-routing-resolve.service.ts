import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFixedAssetDepreciation, FixedAssetDepreciation } from '../fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from '../service/fixed-asset-depreciation.service';

@Injectable({ providedIn: 'root' })
export class FixedAssetDepreciationRoutingResolveService implements Resolve<IFixedAssetDepreciation> {
  constructor(protected service: FixedAssetDepreciationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetDepreciation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fixedAssetDepreciation: HttpResponse<FixedAssetDepreciation>) => {
          if (fixedAssetDepreciation.body) {
            return of(fixedAssetDepreciation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FixedAssetDepreciation());
  }
}
