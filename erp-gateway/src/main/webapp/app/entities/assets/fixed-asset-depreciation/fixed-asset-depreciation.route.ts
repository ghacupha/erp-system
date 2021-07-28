import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFixedAssetDepreciation, FixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';
import { FixedAssetDepreciationService } from './fixed-asset-depreciation.service';
import { FixedAssetDepreciationComponent } from './fixed-asset-depreciation.component';
import { FixedAssetDepreciationDetailComponent } from './fixed-asset-depreciation-detail.component';
import { FixedAssetDepreciationUpdateComponent } from './fixed-asset-depreciation-update.component';

@Injectable({ providedIn: 'root' })
export class FixedAssetDepreciationResolve implements Resolve<IFixedAssetDepreciation> {
  constructor(private service: FixedAssetDepreciationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetDepreciation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fixedAssetDepreciation: HttpResponse<FixedAssetDepreciation>) => {
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

export const fixedAssetDepreciationRoute: Routes = [
  {
    path: '',
    component: FixedAssetDepreciationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FixedAssetDepreciations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetDepreciationDetailComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetDepreciations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetDepreciationUpdateComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetDepreciations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetDepreciationUpdateComponent,
    resolve: {
      fixedAssetDepreciation: FixedAssetDepreciationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetDepreciations',
    },
    canActivate: [UserRouteAccessService],
  },
];
