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
import { ICrbFileTransmissionStatus, getCrbFileTransmissionStatusIdentifier } from '../crb-file-transmission-status.model';

export type EntityResponseType = HttpResponse<ICrbFileTransmissionStatus>;
export type EntityArrayResponseType = HttpResponse<ICrbFileTransmissionStatus[]>;

@Injectable({ providedIn: 'root' })
export class CrbFileTransmissionStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-file-transmission-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-file-transmission-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbFileTransmissionStatus: ICrbFileTransmissionStatus): Observable<EntityResponseType> {
    return this.http.post<ICrbFileTransmissionStatus>(this.resourceUrl, crbFileTransmissionStatus, { observe: 'response' });
  }

  update(crbFileTransmissionStatus: ICrbFileTransmissionStatus): Observable<EntityResponseType> {
    return this.http.put<ICrbFileTransmissionStatus>(
      `${this.resourceUrl}/${getCrbFileTransmissionStatusIdentifier(crbFileTransmissionStatus) as number}`,
      crbFileTransmissionStatus,
      { observe: 'response' }
    );
  }

  partialUpdate(crbFileTransmissionStatus: ICrbFileTransmissionStatus): Observable<EntityResponseType> {
    return this.http.patch<ICrbFileTransmissionStatus>(
      `${this.resourceUrl}/${getCrbFileTransmissionStatusIdentifier(crbFileTransmissionStatus) as number}`,
      crbFileTransmissionStatus,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbFileTransmissionStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbFileTransmissionStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbFileTransmissionStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbFileTransmissionStatusToCollectionIfMissing(
    crbFileTransmissionStatusCollection: ICrbFileTransmissionStatus[],
    ...crbFileTransmissionStatusesToCheck: (ICrbFileTransmissionStatus | null | undefined)[]
  ): ICrbFileTransmissionStatus[] {
    const crbFileTransmissionStatuses: ICrbFileTransmissionStatus[] = crbFileTransmissionStatusesToCheck.filter(isPresent);
    if (crbFileTransmissionStatuses.length > 0) {
      const crbFileTransmissionStatusCollectionIdentifiers = crbFileTransmissionStatusCollection.map(
        crbFileTransmissionStatusItem => getCrbFileTransmissionStatusIdentifier(crbFileTransmissionStatusItem)!
      );
      const crbFileTransmissionStatusesToAdd = crbFileTransmissionStatuses.filter(crbFileTransmissionStatusItem => {
        const crbFileTransmissionStatusIdentifier = getCrbFileTransmissionStatusIdentifier(crbFileTransmissionStatusItem);
        if (
          crbFileTransmissionStatusIdentifier == null ||
          crbFileTransmissionStatusCollectionIdentifiers.includes(crbFileTransmissionStatusIdentifier)
        ) {
          return false;
        }
        crbFileTransmissionStatusCollectionIdentifiers.push(crbFileTransmissionStatusIdentifier);
        return true;
      });
      return [...crbFileTransmissionStatusesToAdd, ...crbFileTransmissionStatusCollection];
    }
    return crbFileTransmissionStatusCollection;
  }
}
