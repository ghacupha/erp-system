import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from '../service/fixed-asset-net-book-value.service';

@Injectable({ providedIn: 'root' })
export class FixedAssetNetBookValueRoutingResolveService implements Resolve<IFixedAssetNetBookValue> {
  constructor(protected service: FixedAssetNetBookValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetNetBookValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fixedAssetNetBookValue: HttpResponse<FixedAssetNetBookValue>) => {
          if (fixedAssetNetBookValue.body) {
            return of(fixedAssetNetBookValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FixedAssetNetBookValue());
  }
}
