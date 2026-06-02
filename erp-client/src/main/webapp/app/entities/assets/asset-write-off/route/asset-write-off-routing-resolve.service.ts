import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetWriteOff, AssetWriteOff } from '../asset-write-off.model';
import { AssetWriteOffService } from '../service/asset-write-off.service';

@Injectable({ providedIn: 'root' })
export class AssetWriteOffRoutingResolveService implements Resolve<IAssetWriteOff> {
  constructor(protected service: AssetWriteOffService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetWriteOff> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetWriteOff: HttpResponse<AssetWriteOff>) => {
          if (assetWriteOff.body) {
            return of(assetWriteOff.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetWriteOff());
  }
}
