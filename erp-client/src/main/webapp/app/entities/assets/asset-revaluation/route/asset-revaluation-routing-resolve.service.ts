import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetRevaluation, AssetRevaluation } from '../asset-revaluation.model';
import { AssetRevaluationService } from '../service/asset-revaluation.service';

@Injectable({ providedIn: 'root' })
export class AssetRevaluationRoutingResolveService implements Resolve<IAssetRevaluation> {
  constructor(protected service: AssetRevaluationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetRevaluation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetRevaluation: HttpResponse<AssetRevaluation>) => {
          if (assetRevaluation.body) {
            return of(assetRevaluation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetRevaluation());
  }
}
