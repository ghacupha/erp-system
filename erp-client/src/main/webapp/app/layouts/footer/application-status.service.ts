///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { IApplicationStatus } from './application-status.model';

export type EntityResponseType = HttpResponse<IApplicationStatus>;

/**
 * Fetch server application status
 */
@Injectable({ providedIn: 'root' })
export class ApplicationStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/application-status');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
  }

  fetch(): Observable<EntityResponseType> {
    return this.http.get<IApplicationStatus>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
