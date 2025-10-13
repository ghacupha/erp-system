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
import { ICrbCustomerType, getCrbCustomerTypeIdentifier } from '../crb-customer-type.model';

export type EntityResponseType = HttpResponse<ICrbCustomerType>;
export type EntityArrayResponseType = HttpResponse<ICrbCustomerType[]>;

@Injectable({ providedIn: 'root' })
export class CrbCustomerTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-customer-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-customer-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbCustomerType: ICrbCustomerType): Observable<EntityResponseType> {
    return this.http.post<ICrbCustomerType>(this.resourceUrl, crbCustomerType, { observe: 'response' });
  }

  update(crbCustomerType: ICrbCustomerType): Observable<EntityResponseType> {
    return this.http.put<ICrbCustomerType>(
      `${this.resourceUrl}/${getCrbCustomerTypeIdentifier(crbCustomerType) as number}`,
      crbCustomerType,
      { observe: 'response' }
    );
  }

  partialUpdate(crbCustomerType: ICrbCustomerType): Observable<EntityResponseType> {
    return this.http.patch<ICrbCustomerType>(
      `${this.resourceUrl}/${getCrbCustomerTypeIdentifier(crbCustomerType) as number}`,
      crbCustomerType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbCustomerType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbCustomerType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbCustomerType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbCustomerTypeToCollectionIfMissing(
    crbCustomerTypeCollection: ICrbCustomerType[],
    ...crbCustomerTypesToCheck: (ICrbCustomerType | null | undefined)[]
  ): ICrbCustomerType[] {
    const crbCustomerTypes: ICrbCustomerType[] = crbCustomerTypesToCheck.filter(isPresent);
    if (crbCustomerTypes.length > 0) {
      const crbCustomerTypeCollectionIdentifiers = crbCustomerTypeCollection.map(
        crbCustomerTypeItem => getCrbCustomerTypeIdentifier(crbCustomerTypeItem)!
      );
      const crbCustomerTypesToAdd = crbCustomerTypes.filter(crbCustomerTypeItem => {
        const crbCustomerTypeIdentifier = getCrbCustomerTypeIdentifier(crbCustomerTypeItem);
        if (crbCustomerTypeIdentifier == null || crbCustomerTypeCollectionIdentifiers.includes(crbCustomerTypeIdentifier)) {
          return false;
        }
        crbCustomerTypeCollectionIdentifiers.push(crbCustomerTypeIdentifier);
        return true;
      });
      return [...crbCustomerTypesToAdd, ...crbCustomerTypeCollection];
    }
    return crbCustomerTypeCollection;
  }
}
