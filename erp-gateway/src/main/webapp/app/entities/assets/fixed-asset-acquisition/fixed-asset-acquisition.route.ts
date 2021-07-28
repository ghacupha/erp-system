import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFixedAssetAcquisition, FixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';
import { FixedAssetAcquisitionService } from './fixed-asset-acquisition.service';
import { FixedAssetAcquisitionComponent } from './fixed-asset-acquisition.component';
import { FixedAssetAcquisitionDetailComponent } from './fixed-asset-acquisition-detail.component';
import { FixedAssetAcquisitionUpdateComponent } from './fixed-asset-acquisition-update.component';

@Injectable({ providedIn: 'root' })
export class FixedAssetAcquisitionResolve implements Resolve<IFixedAssetAcquisition> {
  constructor(private service: FixedAssetAcquisitionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetAcquisition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fixedAssetAcquisition: HttpResponse<FixedAssetAcquisition>) => {
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

export const fixedAssetAcquisitionRoute: Routes = [
  {
    path: '',
    component: FixedAssetAcquisitionComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FixedAssetAcquisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetAcquisitionDetailComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetAcquisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetAcquisitionUpdateComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetAcquisitions',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetAcquisitionUpdateComponent,
    resolve: {
      fixedAssetAcquisition: FixedAssetAcquisitionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetAcquisitions',
    },
    canActivate: [UserRouteAccessService],
  },
];
