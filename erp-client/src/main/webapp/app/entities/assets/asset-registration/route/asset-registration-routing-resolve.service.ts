import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetRegistration, AssetRegistration } from '../asset-registration.model';
import { AssetRegistrationService } from '../service/asset-registration.service';

@Injectable({ providedIn: 'root' })
export class AssetRegistrationRoutingResolveService implements Resolve<IAssetRegistration> {
  constructor(protected service: AssetRegistrationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetRegistration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetRegistration: HttpResponse<AssetRegistration>) => {
          if (assetRegistration.body) {
            return of(assetRegistration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetRegistration());
  }
}
