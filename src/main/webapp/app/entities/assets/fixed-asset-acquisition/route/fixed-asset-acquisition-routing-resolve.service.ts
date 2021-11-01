import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFixedAssetAcquisition, FixedAssetAcquisition } from '../fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from '../service/fixed-asset-acquisition.service';

@Injectable({ providedIn: 'root' })
export class FixedAssetAcquisitionRoutingResolveService implements Resolve<IFixedAssetAcquisition> {
  constructor(protected service: FixedAssetAcquisitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetAcquisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fixedAssetAcquisition: HttpResponse<FixedAssetAcquisition>) => {
          if (fixedAssetAcquisition.body) {
            return of(fixedAssetAcquisition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FixedAssetAcquisition());
  }
}
