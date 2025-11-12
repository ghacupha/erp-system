///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { createRequestOption } from 'app/core/request/request-util';
import { IReportMetadata } from './report-metadata.model';

@Injectable({ providedIn: 'root' })
export class ReportMetadataService {
  private readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/app/report-metadata');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  search(term: string, size = 10): Observable<IReportMetadata[]> {
    const params = createRequestOption({ term: term?.trim() ?? '', size });
    return this.http
      .get<IReportMetadata[]>(`${this.resourceUrl}/search`, { params })
      .pipe(map(response => response ?? []));
  }

  findByPagePath(pagePath: string): Observable<IReportMetadata | null> {
    const params = new HttpParams().set('path', pagePath);
    return this.http
      .get<IReportMetadata>(`${this.resourceUrl}/by-path`, { params })
      .pipe(map(metadata => metadata ?? null));
  }

  listActive(size = 20): Observable<IReportMetadata[]> {
    const params = createRequestOption({ size });
    return this.http
      .get<IReportMetadata[]>(`${this.resourceUrl}/active`, { params })
      .pipe(map(response => response ?? []));
  }
}
