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

import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, SecurityContext, NgZone } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

export type AlertType = 'success' | 'danger' | 'warning' | 'info';

export interface Alert {
  id?: number;
  type: AlertType;
  message?: string;
  timeout?: number;
  toast?: boolean;
  position?: string;
  close?: (alerts: Alert[]) => void;
}

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  timeout = 5000;
  toast = false;
  position = 'top right';

  // unique id for each alert. Starts from 0.
  private alertId = 0;
  private alerts: Alert[] = [];

  constructor(private sanitizer: DomSanitizer, private ngZone: NgZone) {}

  clear(): void {
    this.alerts = [];
  }

  get(): Alert[] {
    return this.alerts;
  }

  /**
   * Adds alert to alerts array and returns added alert.
   * @param alert      Alert to add. If `timeout`, `toast` or `position` is missing then applying default value.
   * @param extAlerts  If missing then adding `alert` to `AlertService` internal array and alerts can be retrieved by `get()`.
   *                   Else adding `alert` to `extAlerts`.
   * @returns  Added alert
   */
  addAlert(alert: Alert, extAlerts?: Alert[]): Alert {
    alert.id = this.alertId++;

    alert.message = this.sanitizer.sanitize(SecurityContext.HTML, alert.message ?? '') ?? '';
    alert.timeout = alert.timeout ?? this.timeout;
    alert.toast = alert.toast ?? this.toast;
    alert.position = alert.position ?? this.position;
    alert.close = (alertsArray: Alert[]) => this.closeAlert(alert.id!, alertsArray);

    (extAlerts ?? this.alerts).push(alert);

    if (alert.timeout > 0) {
      // Workaround protractor waiting for setTimeout.
      // Reference https://www.protractortest.org/#/timeouts
      this.ngZone.runOutsideAngular(() => {
        setTimeout(() => {
          this.ngZone.run(() => {
            this.closeAlert(alert.id!, extAlerts ?? this.alerts);
          });
        }, alert.timeout);
      });
    }

    return alert;
  }

  private closeAlert(alertId: number, extAlerts?: Alert[]): void {
    const alerts = extAlerts ?? this.alerts;
    const alertIndex = alerts.map(alert => alert.id).indexOf(alertId);
    // if found alert then remove
    if (alertIndex >= 0) {
      alerts.splice(alertIndex, 1);
    }
  }

  addHttpErrorResponse(error: HttpErrorResponse, extAlerts?: Alert[]): void {
    const errorMessage =
      (error.error && typeof error.error === 'object' && (error.error.detail ?? error.error.message)) ||
      (typeof error.error === 'string' ? error.error : undefined) ||
      error.message ||
      'An unexpected error occurred';

    this.addAlert({ type: 'danger', message: errorMessage }, extAlerts);
  }
}
