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
import { ISystemContentType, getSystemContentTypeIdentifier } from '../system-content-type.model';

export type EntityResponseType = HttpResponse<ISystemContentType>;
export type EntityArrayResponseType = HttpResponse<ISystemContentType[]>;

@Injectable({ providedIn: 'root' })
export class SystemContentTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-content-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/system-content-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemContentType: ISystemContentType): Observable<EntityResponseType> {
    return this.http.post<ISystemContentType>(this.resourceUrl, systemContentType, { observe: 'response' });
  }

  update(systemContentType: ISystemContentType): Observable<EntityResponseType> {
    return this.http.put<ISystemContentType>(
      `${this.resourceUrl}/${getSystemContentTypeIdentifier(systemContentType) as number}`,
      systemContentType,
      { observe: 'response' }
    );
  }

  partialUpdate(systemContentType: ISystemContentType): Observable<EntityResponseType> {
    return this.http.patch<ISystemContentType>(
      `${this.resourceUrl}/${getSystemContentTypeIdentifier(systemContentType) as number}`,
      systemContentType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISystemContentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISystemContentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISystemContentType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSystemContentTypeToCollectionIfMissing(
    systemContentTypeCollection: ISystemContentType[],
    ...systemContentTypesToCheck: (ISystemContentType | null | undefined)[]
  ): ISystemContentType[] {
    const systemContentTypes: ISystemContentType[] = systemContentTypesToCheck.filter(isPresent);
    if (systemContentTypes.length > 0) {
      const systemContentTypeCollectionIdentifiers = systemContentTypeCollection.map(
        systemContentTypeItem => getSystemContentTypeIdentifier(systemContentTypeItem)!
      );
      const systemContentTypesToAdd = systemContentTypes.filter(systemContentTypeItem => {
        const systemContentTypeIdentifier = getSystemContentTypeIdentifier(systemContentTypeItem);
        if (systemContentTypeIdentifier == null || systemContentTypeCollectionIdentifiers.includes(systemContentTypeIdentifier)) {
          return false;
        }
        systemContentTypeCollectionIdentifiers.push(systemContentTypeIdentifier);
        return true;
      });
      return [...systemContentTypesToAdd, ...systemContentTypeCollection];
    }
    return systemContentTypeCollection;
  }
}
