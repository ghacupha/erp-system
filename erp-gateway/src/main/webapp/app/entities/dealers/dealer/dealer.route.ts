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
import { IDealer, Dealer } from 'app/shared/model/dealers/dealer.model';
import { DealerService } from './dealer.service';
import { DealerComponent } from './dealer.component';
import { DealerDetailComponent } from './dealer-detail.component';
import { DealerUpdateComponent } from './dealer-update.component';

@Injectable({ providedIn: 'root' })
export class DealerResolve implements Resolve<IDealer> {
  constructor(private service: DealerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDealer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dealer: HttpResponse<Dealer>) => {
          if (dealer.body) {
            return of(dealer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dealer());
  }
}

export const dealerRoute: Routes = [
  {
    path: '',
    component: DealerComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Dealers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DealerDetailComponent,
    resolve: {
      dealer: DealerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dealers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DealerUpdateComponent,
    resolve: {
      dealer: DealerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dealers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DealerUpdateComponent,
    resolve: {
      dealer: DealerResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Dealers',
    },
    canActivate: [UserRouteAccessService],
  },
];
