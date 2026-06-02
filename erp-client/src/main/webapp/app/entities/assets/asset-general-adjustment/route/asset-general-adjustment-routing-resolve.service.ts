import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetGeneralAdjustment, AssetGeneralAdjustment } from '../asset-general-adjustment.model';
import { AssetGeneralAdjustmentService } from '../service/asset-general-adjustment.service';

@Injectable({ providedIn: 'root' })
export class AssetGeneralAdjustmentRoutingResolveService implements Resolve<IAssetGeneralAdjustment> {
  constructor(protected service: AssetGeneralAdjustmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetGeneralAdjustment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetGeneralAdjustment: HttpResponse<AssetGeneralAdjustment>) => {
          if (assetGeneralAdjustment.body) {
            return of(assetGeneralAdjustment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetGeneralAdjustment());
  }
}
