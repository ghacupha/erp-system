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

import { Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { StateStorageService } from 'app/core/auth/state-storage.service';

const POLL_INTERVAL_SECONDS = 10;

@Component({
  selector: 'jhi-maintenance',
  templateUrl: './maintenance.component.html',
  styleUrls: ['./maintenance.component.scss'],
})
export class MaintenanceComponent implements OnInit, OnDestroy {
  checking = false;
  countdown = POLL_INTERVAL_SECONDS;

  private timer?: ReturnType<typeof setInterval>;

  constructor(private http: HttpClient, private router: Router, private stateStorageService: StateStorageService, private ngZone: NgZone) {}

  ngOnInit(): void {
    this.ngZone.runOutsideAngular(() => {
      this.timer = setInterval(() => {
        this.ngZone.run(() => {
          this.countdown -= 1;
          if (this.countdown <= 0) {
            this.retryNow();
          }
        });
      }, 1000);
    });
  }

  ngOnDestroy(): void {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  retryNow(): void {
    if (this.checking) {
      return;
    }
    this.checking = true;
    this.countdown = POLL_INTERVAL_SECONDS;
    this.http.get('management/health/readiness', { observe: 'response' }).subscribe({
      next: () => {
        this.checking = false;
        const target = this.stateStorageService.getUrl() ?? '/';
        this.stateStorageService.clearUrl();
        this.router.navigateByUrl(target);
      },
      error: () => {
        this.checking = false;
      },
    });
  }
}
