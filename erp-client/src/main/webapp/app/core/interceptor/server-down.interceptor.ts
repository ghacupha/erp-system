///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, tap, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { Router } from '@angular/router';

import { StateStorageService } from 'app/core/auth/state-storage.service';

/**
 * Sends the user to the /maintenance page when the application server is genuinely
 * unreachable.
 *
 * A single failed request is not enough: an aborted request (the user navigating away)
 * also surfaces as status 0. So when a backend call fails with a "server down" status
 * (0 / 502 / 503 / 504) this interceptor fires one confirmation probe against the
 * readiness endpoint (bypassing the interceptor chain). Only if that probe also fails
 * do we redirect to /maintenance. The maintenance page then polls readiness and
 * navigates the user back once the server recovers.
 */
@Injectable()
export class ServerDownInterceptor implements HttpInterceptor {
  private static readonly SERVER_DOWN_STATUSES = [0, 502, 503, 504];
  private static readonly PROBE_TIMEOUT_MS = 4000;

  private readonly probeClient: HttpClient;
  private probing = false;

  constructor(private router: Router, private stateStorageService: StateStorageService, httpBackend: HttpBackend) {
    // Bypass the interceptor chain for the probe so it can't recurse into this interceptor.
    this.probeClient = new HttpClient(httpBackend);
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap({
        error: (err: HttpErrorResponse) => {
          if (!(err instanceof HttpErrorResponse) || !ServerDownInterceptor.SERVER_DOWN_STATUSES.includes(err.status)) {
            return;
          }
          if (!this.isBackendCall(request.url) || this.isHealthCall(request.url)) {
            return;
          }
          if (this.probing || this.router.routerState.snapshot.url.startsWith('/maintenance')) {
            return;
          }
          this.confirmAndRedirect();
        },
      })
    );
  }

  private confirmAndRedirect(): void {
    this.probing = true;
    this.probeClient
      .get('management/health/readiness', { observe: 'response' })
      .pipe(
        timeout(ServerDownInterceptor.PROBE_TIMEOUT_MS),
        catchError(() => of(null))
      )
      .subscribe(response => {
        this.probing = false;
        if (response) {
          return; // server answered - the original failure was transient / an aborted call
        }
        const currentUrl = this.router.routerState.snapshot.url;
        if (currentUrl.startsWith('/maintenance')) {
          return;
        }
        this.stateStorageService.storeUrl(currentUrl);
        this.router.navigate(['/maintenance']);
      });
  }

  private isBackendCall(url: string): boolean {
    return /(^|\/)(api|services|management)\//.test(url) || /(^|\/)(api|services|management)$/.test(url);
  }

  private isHealthCall(url: string): boolean {
    return url.includes('management/health');
  }
}
