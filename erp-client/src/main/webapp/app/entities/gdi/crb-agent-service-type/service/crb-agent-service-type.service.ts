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
import { ICrbAgentServiceType, getCrbAgentServiceTypeIdentifier } from '../crb-agent-service-type.model';

export type EntityResponseType = HttpResponse<ICrbAgentServiceType>;
export type EntityArrayResponseType = HttpResponse<ICrbAgentServiceType[]>;

@Injectable({ providedIn: 'root' })
export class CrbAgentServiceTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-agent-service-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-agent-service-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbAgentServiceType: ICrbAgentServiceType): Observable<EntityResponseType> {
    return this.http.post<ICrbAgentServiceType>(this.resourceUrl, crbAgentServiceType, { observe: 'response' });
  }

  update(crbAgentServiceType: ICrbAgentServiceType): Observable<EntityResponseType> {
    return this.http.put<ICrbAgentServiceType>(
      `${this.resourceUrl}/${getCrbAgentServiceTypeIdentifier(crbAgentServiceType) as number}`,
      crbAgentServiceType,
      { observe: 'response' }
    );
  }

  partialUpdate(crbAgentServiceType: ICrbAgentServiceType): Observable<EntityResponseType> {
    return this.http.patch<ICrbAgentServiceType>(
      `${this.resourceUrl}/${getCrbAgentServiceTypeIdentifier(crbAgentServiceType) as number}`,
      crbAgentServiceType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbAgentServiceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbAgentServiceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbAgentServiceType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbAgentServiceTypeToCollectionIfMissing(
    crbAgentServiceTypeCollection: ICrbAgentServiceType[],
    ...crbAgentServiceTypesToCheck: (ICrbAgentServiceType | null | undefined)[]
  ): ICrbAgentServiceType[] {
    const crbAgentServiceTypes: ICrbAgentServiceType[] = crbAgentServiceTypesToCheck.filter(isPresent);
    if (crbAgentServiceTypes.length > 0) {
      const crbAgentServiceTypeCollectionIdentifiers = crbAgentServiceTypeCollection.map(
        crbAgentServiceTypeItem => getCrbAgentServiceTypeIdentifier(crbAgentServiceTypeItem)!
      );
      const crbAgentServiceTypesToAdd = crbAgentServiceTypes.filter(crbAgentServiceTypeItem => {
        const crbAgentServiceTypeIdentifier = getCrbAgentServiceTypeIdentifier(crbAgentServiceTypeItem);
        if (crbAgentServiceTypeIdentifier == null || crbAgentServiceTypeCollectionIdentifiers.includes(crbAgentServiceTypeIdentifier)) {
          return false;
        }
        crbAgentServiceTypeCollectionIdentifiers.push(crbAgentServiceTypeIdentifier);
        return true;
      });
      return [...crbAgentServiceTypesToAdd, ...crbAgentServiceTypeCollection];
    }
    return crbAgentServiceTypeCollection;
  }
}
