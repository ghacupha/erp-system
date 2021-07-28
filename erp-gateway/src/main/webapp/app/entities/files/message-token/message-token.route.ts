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
import { IMessageToken, MessageToken } from 'app/shared/model/files/message-token.model';
import { MessageTokenService } from './message-token.service';
import { MessageTokenComponent } from './message-token.component';
import { MessageTokenDetailComponent } from './message-token-detail.component';
import { MessageTokenUpdateComponent } from './message-token-update.component';

@Injectable({ providedIn: 'root' })
export class MessageTokenResolve implements Resolve<IMessageToken> {
  constructor(private service: MessageTokenService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMessageToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((messageToken: HttpResponse<MessageToken>) => {
          if (messageToken.body) {
            return of(messageToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MessageToken());
  }
}

export const messageTokenRoute: Routes = [
  {
    path: '',
    component: MessageTokenComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'MessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MessageTokenDetailComponent,
    resolve: {
      messageToken: MessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MessageTokenUpdateComponent,
    resolve: {
      messageToken: MessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MessageTokenUpdateComponent,
    resolve: {
      messageToken: MessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
];
