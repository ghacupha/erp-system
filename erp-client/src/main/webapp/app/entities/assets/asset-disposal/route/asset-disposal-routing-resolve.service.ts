import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetDisposal, AssetDisposal } from '../asset-disposal.model';
import { AssetDisposalService } from '../service/asset-disposal.service';

@Injectable({ providedIn: 'root' })
export class AssetDisposalRoutingResolveService implements Resolve<IAssetDisposal> {
  constructor(protected service: AssetDisposalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetDisposal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetDisposal: HttpResponse<AssetDisposal>) => {
          if (assetDisposal.body) {
            return of(assetDisposal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetDisposal());
  }
}
