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
import { ITaxReference, TaxReference } from 'app/shared/model/payments/tax-reference.model';
import { TaxReferenceService } from './tax-reference.service';
import { TaxReferenceComponent } from './tax-reference.component';
import { TaxReferenceDetailComponent } from './tax-reference-detail.component';
import { TaxReferenceUpdateComponent } from './tax-reference-update.component';

@Injectable({ providedIn: 'root' })
export class TaxReferenceResolve implements Resolve<ITaxReference> {
  constructor(private service: TaxReferenceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITaxReference> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((taxReference: HttpResponse<TaxReference>) => {
          if (taxReference.body) {
            return of(taxReference.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TaxReference());
  }
}

export const taxReferenceRoute: Routes = [
  {
    path: '',
    component: TaxReferenceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TaxReferences',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TaxReferenceDetailComponent,
    resolve: {
      taxReference: TaxReferenceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxReferences',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TaxReferenceUpdateComponent,
    resolve: {
      taxReference: TaxReferenceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxReferences',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TaxReferenceUpdateComponent,
    resolve: {
      taxReference: TaxReferenceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TaxReferences',
    },
    canActivate: [UserRouteAccessService],
  },
];
