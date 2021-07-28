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
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { LoginService } from 'app/core/login/login.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {
  constructor(
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private stateStorageService: StateStorageService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(null, (err: HttpErrorResponse) => {
        if (err.status === 401 && err.url && !err.url.includes('api/account')) {
          if (err.url.includes(this.loginService.logoutUrl())) {
            this.loginService.logoutInClient();
            return;
          }
          this.stateStorageService.storeUrl(this.router.routerState.snapshot.url);
          this.loginService.logout();
          this.router.navigate(['']);
          this.loginModalService.open();
        }
      })
    );
  }
}
