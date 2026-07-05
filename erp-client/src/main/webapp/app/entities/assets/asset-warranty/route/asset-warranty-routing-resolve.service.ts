import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetWarranty, AssetWarranty } from '../asset-warranty.model';
import { AssetWarrantyService } from '../service/asset-warranty.service';

@Injectable({ providedIn: 'root' })
export class AssetWarrantyRoutingResolveService implements Resolve<IAssetWarranty> {
  constructor(protected service: AssetWarrantyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetWarranty> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetWarranty: HttpResponse<AssetWarranty>) => {
          if (assetWarranty.body) {
            return of(assetWarranty.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetWarranty());
  }
}
