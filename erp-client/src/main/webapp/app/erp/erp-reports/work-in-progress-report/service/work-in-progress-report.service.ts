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
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkInProgressReport, getWorkInProgressReportIdentifier } from '../work-in-progress-report.model';

export type EntityResponseType = HttpResponse<IWorkInProgressReport>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressReport[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/work-in-progress-summary/reported');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/work-in-progress-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkInProgressReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkInProgressReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByReportDate(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkInProgressReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkInProgressReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addWorkInProgressReportToCollectionIfMissing(
    workInProgressReportCollection: IWorkInProgressReport[],
    ...workInProgressReportsToCheck: (IWorkInProgressReport | null | undefined)[]
  ): IWorkInProgressReport[] {
    const workInProgressReports: IWorkInProgressReport[] = workInProgressReportsToCheck.filter(isPresent);
    if (workInProgressReports.length > 0) {
      const workInProgressReportCollectionIdentifiers = workInProgressReportCollection.map(
        workInProgressReportItem => getWorkInProgressReportIdentifier(workInProgressReportItem)!
      );
      const workInProgressReportsToAdd = workInProgressReports.filter(workInProgressReportItem => {
        const workInProgressReportIdentifier = getWorkInProgressReportIdentifier(workInProgressReportItem);
        if (workInProgressReportIdentifier == null || workInProgressReportCollectionIdentifiers.includes(workInProgressReportIdentifier)) {
          return false;
        }
        workInProgressReportCollectionIdentifiers.push(workInProgressReportIdentifier);
        return true;
      });
      return [...workInProgressReportsToAdd, ...workInProgressReportCollection];
    }
    return workInProgressReportCollection;
  }
}
