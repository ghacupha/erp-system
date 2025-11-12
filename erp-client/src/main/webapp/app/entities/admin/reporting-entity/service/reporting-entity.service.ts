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
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportingEntity, getReportingEntityIdentifier } from '../reporting-entity.model';

export type EntityResponseType = HttpResponse<IReportingEntity>;
export type EntityArrayResponseType = HttpResponse<IReportingEntity[]>;

@Injectable({ providedIn: 'root' })
export class ReportingEntityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-entities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/reporting-entities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportingEntity: IReportingEntity): Observable<EntityResponseType> {
    return this.http.post<IReportingEntity>(this.resourceUrl, reportingEntity, { observe: 'response' });
  }

  update(reportingEntity: IReportingEntity): Observable<EntityResponseType> {
    return this.http.put<IReportingEntity>(
      `${this.resourceUrl}/${getReportingEntityIdentifier(reportingEntity) as number}`,
      reportingEntity,
      { observe: 'response' }
    );
  }

  partialUpdate(reportingEntity: IReportingEntity): Observable<EntityResponseType> {
    return this.http.patch<IReportingEntity>(
      `${this.resourceUrl}/${getReportingEntityIdentifier(reportingEntity) as number}`,
      reportingEntity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportingEntity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportingEntity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportingEntity[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addReportingEntityToCollectionIfMissing(
    reportingEntityCollection: IReportingEntity[],
    ...reportingEntitiesToCheck: (IReportingEntity | null | undefined)[]
  ): IReportingEntity[] {
    const reportingEntities: IReportingEntity[] = reportingEntitiesToCheck.filter(isPresent);
    if (reportingEntities.length > 0) {
      const reportingEntityCollectionIdentifiers = reportingEntityCollection.map(
        reportingEntityItem => getReportingEntityIdentifier(reportingEntityItem)!
      );
      const reportingEntitiesToAdd = reportingEntities.filter(reportingEntityItem => {
        const reportingEntityIdentifier = getReportingEntityIdentifier(reportingEntityItem);
        if (reportingEntityIdentifier == null || reportingEntityCollectionIdentifiers.includes(reportingEntityIdentifier)) {
          return false;
        }
        reportingEntityCollectionIdentifiers.push(reportingEntityIdentifier);
        return true;
      });
      return [...reportingEntitiesToAdd, ...reportingEntityCollection];
    }
    return reportingEntityCollection;
  }
}
