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
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IWorkInProgressOverview } from '../work-in-progress-overview.model';

export type EntityResponseType = HttpResponse<IWorkInProgressOverview>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressOverview[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressOverviewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/work-in-progress-overview');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  findByDate(reportDate: string): Observable<EntityResponseType> {

    let reportParams: HttpParams = new HttpParams();
    reportParams = reportParams.set('reportDate', reportDate);

    return this.http.get<IWorkInProgressOverview>(`${this.resourceUrl}`, { params: reportParams, observe: 'response' });
  }
}
