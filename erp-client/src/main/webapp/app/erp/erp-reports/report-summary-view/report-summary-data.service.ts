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
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ReportSummaryRecord } from '../report-metadata/report-metadata.model';

@Injectable({ providedIn: 'root' })
export class ReportSummaryDataService {
  private readonly defaultSize = 200;

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  fetchSummary(apiPath: string, params?: Record<string, unknown>): Observable<ReportSummaryRecord[]> {
    const endpoint = this.resolveEndpoint(apiPath);
    const { url, remainingParams } = this.applyPathParams(endpoint, params);
    const httpParams = this.buildParams(remainingParams);
    return this.http
      .get<ReportSummaryRecord[]>(url, { params: httpParams })
      .pipe(map(response => response ?? []));
  }

  private resolveEndpoint(apiPath: string): string {
    if (apiPath.toLowerCase().startsWith('http')) {
      return apiPath;
    }
    const sanitized = apiPath.startsWith('/') ? apiPath.substring(1) : apiPath;
    return this.applicationConfigService.getEndpointFor(sanitized);
  }

  private applyPathParams(
    endpoint: string,
    params?: Record<string, unknown>
  ): { url: string; remainingParams?: Record<string, unknown> } {
    if (!params) {
      return { url: endpoint, remainingParams: undefined };
    }

    const placeholders = endpoint.match(/\{[^}]+\}/g);
    if (!placeholders || placeholders.length === 0) {
      return { url: endpoint, remainingParams: params };
    }

    const remainingParams: Record<string, unknown> = { ...params };
    let resolvedUrl = endpoint;

    placeholders.forEach(placeholder => {
      const key = placeholder.slice(1, -1);
      if (!Object.prototype.hasOwnProperty.call(remainingParams, key)) {
        return;
      }
      const value = remainingParams[key];
      delete remainingParams[key];
      if (value === undefined || value === null || value === '') {
        return;
      }
      resolvedUrl = resolvedUrl.replace(placeholder, encodeURIComponent(String(value)));
    });

    return { url: resolvedUrl, remainingParams };
  }

  private buildParams(params?: Record<string, unknown>): HttpParams {
    let httpParams = new HttpParams().set('size', this.defaultSize.toString());
    if (!params) {
      return httpParams;
    }
    Object.keys(params).forEach(key => {
      const value = params[key];
      if (value !== undefined && value !== null && value !== '') {
        httpParams = httpParams.set(key, String(value));
      }
    });
    return httpParams;
  }
}
