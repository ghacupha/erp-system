///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';
import { FixedAssetNetBookValueService } from './fixed-asset-net-book-value.service';
import { FixedAssetNetBookValueComponent } from './fixed-asset-net-book-value.component';
import { FixedAssetNetBookValueDetailComponent } from './fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValueUpdateComponent } from './fixed-asset-net-book-value-update.component';

@Injectable({ providedIn: 'root' })
export class FixedAssetNetBookValueResolve implements Resolve<IFixedAssetNetBookValue> {
  constructor(private service: FixedAssetNetBookValueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFixedAssetNetBookValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fixedAssetNetBookValue: HttpResponse<FixedAssetNetBookValue>) => {
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

export const fixedAssetNetBookValueRoute: Routes = [
  {
    path: '',
    component: FixedAssetNetBookValueComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FixedAssetNetBookValues',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FixedAssetNetBookValueDetailComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetNetBookValues',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FixedAssetNetBookValueUpdateComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetNetBookValues',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FixedAssetNetBookValueUpdateComponent,
    resolve: {
      fixedAssetNetBookValue: FixedAssetNetBookValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FixedAssetNetBookValues',
    },
    canActivate: [UserRouteAccessService],
  },
];
