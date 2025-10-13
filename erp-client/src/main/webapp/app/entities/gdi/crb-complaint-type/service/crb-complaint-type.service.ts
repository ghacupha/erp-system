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
import { ICrbComplaintType, getCrbComplaintTypeIdentifier } from '../crb-complaint-type.model';

export type EntityResponseType = HttpResponse<ICrbComplaintType>;
export type EntityArrayResponseType = HttpResponse<ICrbComplaintType[]>;

@Injectable({ providedIn: 'root' })
export class CrbComplaintTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-complaint-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-complaint-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbComplaintType: ICrbComplaintType): Observable<EntityResponseType> {
    return this.http.post<ICrbComplaintType>(this.resourceUrl, crbComplaintType, { observe: 'response' });
  }

  update(crbComplaintType: ICrbComplaintType): Observable<EntityResponseType> {
    return this.http.put<ICrbComplaintType>(
      `${this.resourceUrl}/${getCrbComplaintTypeIdentifier(crbComplaintType) as number}`,
      crbComplaintType,
      { observe: 'response' }
    );
  }

  partialUpdate(crbComplaintType: ICrbComplaintType): Observable<EntityResponseType> {
    return this.http.patch<ICrbComplaintType>(
      `${this.resourceUrl}/${getCrbComplaintTypeIdentifier(crbComplaintType) as number}`,
      crbComplaintType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbComplaintType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbComplaintType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbComplaintType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbComplaintTypeToCollectionIfMissing(
    crbComplaintTypeCollection: ICrbComplaintType[],
    ...crbComplaintTypesToCheck: (ICrbComplaintType | null | undefined)[]
  ): ICrbComplaintType[] {
    const crbComplaintTypes: ICrbComplaintType[] = crbComplaintTypesToCheck.filter(isPresent);
    if (crbComplaintTypes.length > 0) {
      const crbComplaintTypeCollectionIdentifiers = crbComplaintTypeCollection.map(
        crbComplaintTypeItem => getCrbComplaintTypeIdentifier(crbComplaintTypeItem)!
      );
      const crbComplaintTypesToAdd = crbComplaintTypes.filter(crbComplaintTypeItem => {
        const crbComplaintTypeIdentifier = getCrbComplaintTypeIdentifier(crbComplaintTypeItem);
        if (crbComplaintTypeIdentifier == null || crbComplaintTypeCollectionIdentifiers.includes(crbComplaintTypeIdentifier)) {
          return false;
        }
        crbComplaintTypeCollectionIdentifiers.push(crbComplaintTypeIdentifier);
        return true;
      });
      return [...crbComplaintTypesToAdd, ...crbComplaintTypeCollection];
    }
    return crbComplaintTypeCollection;
  }
}
