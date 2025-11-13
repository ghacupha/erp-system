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

/**
 * Just noting that the class method #setEndpointPrefix is being used to set the API_URL
 * at compile time. So there being no other evidence, am going to take a gamble and disable
 * the #endpointPrefix field in order to prevent the values being generated such as
 * http://localhost:8980/underfinedapi/account running on the client calls.
 * Someone else is adding the API URL before ${this.endpointPrefix}.
 */
@Injectable({
  providedIn: 'root',
})
export class ApplicationConfigService {
  private endpointPrefix: string | undefined = '';
  private microfrontend = false;

  setEndpointPrefix(endpointPrefix: string|undefined): void {
    this.endpointPrefix = endpointPrefix;
  }

  setMicrofrontend(microfrontend = true): void {
    this.microfrontend = microfrontend;
  }

  isMicrofrontend(): boolean {
    return this.microfrontend;
  }

  getEndpointFor(api: string, microservice?: string): string {
    if (microservice) {
      if (this.endpointPrefix === undefined) {
        return `services/${microservice}/${api}`;
      }
      return `${this.endpointPrefix}services/${microservice}/${api}`;
    }
    if (this.endpointPrefix === undefined) {
      return `${api}`;
    }
    return `${this.endpointPrefix}${api}`;
  }
}
