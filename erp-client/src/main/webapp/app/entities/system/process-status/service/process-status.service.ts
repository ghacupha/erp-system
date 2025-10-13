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
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProcessStatus, getProcessStatusIdentifier } from '../process-status.model';

export type EntityResponseType = HttpResponse<IProcessStatus>;
export type EntityArrayResponseType = HttpResponse<IProcessStatus[]>;

@Injectable({ providedIn: 'root' })
export class ProcessStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/process-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/process-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(processStatus: IProcessStatus): Observable<EntityResponseType> {
    return this.http.post<IProcessStatus>(this.resourceUrl, processStatus, { observe: 'response' });
  }

  update(processStatus: IProcessStatus): Observable<EntityResponseType> {
    return this.http.put<IProcessStatus>(`${this.resourceUrl}/${getProcessStatusIdentifier(processStatus) as number}`, processStatus, {
      observe: 'response',
    });
  }

  partialUpdate(processStatus: IProcessStatus): Observable<EntityResponseType> {
    return this.http.patch<IProcessStatus>(`${this.resourceUrl}/${getProcessStatusIdentifier(processStatus) as number}`, processStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProcessStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProcessStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProcessStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addProcessStatusToCollectionIfMissing(
    processStatusCollection: IProcessStatus[],
    ...processStatusesToCheck: (IProcessStatus | null | undefined)[]
  ): IProcessStatus[] {
    const processStatuses: IProcessStatus[] = processStatusesToCheck.filter(isPresent);
    if (processStatuses.length > 0) {
      const processStatusCollectionIdentifiers = processStatusCollection.map(
        processStatusItem => getProcessStatusIdentifier(processStatusItem)!
      );
      const processStatusesToAdd = processStatuses.filter(processStatusItem => {
        const processStatusIdentifier = getProcessStatusIdentifier(processStatusItem);
        if (processStatusIdentifier == null || processStatusCollectionIdentifiers.includes(processStatusIdentifier)) {
          return false;
        }
        processStatusCollectionIdentifiers.push(processStatusIdentifier);
        return true;
      });
      return [...processStatusesToAdd, ...processStatusCollection];
    }
    return processStatusCollection;
  }
}
