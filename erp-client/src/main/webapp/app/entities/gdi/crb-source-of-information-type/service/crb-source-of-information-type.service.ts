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
import { ICrbSourceOfInformationType, getCrbSourceOfInformationTypeIdentifier } from '../crb-source-of-information-type.model';

export type EntityResponseType = HttpResponse<ICrbSourceOfInformationType>;
export type EntityArrayResponseType = HttpResponse<ICrbSourceOfInformationType[]>;

@Injectable({ providedIn: 'root' })
export class CrbSourceOfInformationTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-source-of-information-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-source-of-information-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbSourceOfInformationType: ICrbSourceOfInformationType): Observable<EntityResponseType> {
    return this.http.post<ICrbSourceOfInformationType>(this.resourceUrl, crbSourceOfInformationType, { observe: 'response' });
  }

  update(crbSourceOfInformationType: ICrbSourceOfInformationType): Observable<EntityResponseType> {
    return this.http.put<ICrbSourceOfInformationType>(
      `${this.resourceUrl}/${getCrbSourceOfInformationTypeIdentifier(crbSourceOfInformationType) as number}`,
      crbSourceOfInformationType,
      { observe: 'response' }
    );
  }

  partialUpdate(crbSourceOfInformationType: ICrbSourceOfInformationType): Observable<EntityResponseType> {
    return this.http.patch<ICrbSourceOfInformationType>(
      `${this.resourceUrl}/${getCrbSourceOfInformationTypeIdentifier(crbSourceOfInformationType) as number}`,
      crbSourceOfInformationType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbSourceOfInformationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbSourceOfInformationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbSourceOfInformationType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbSourceOfInformationTypeToCollectionIfMissing(
    crbSourceOfInformationTypeCollection: ICrbSourceOfInformationType[],
    ...crbSourceOfInformationTypesToCheck: (ICrbSourceOfInformationType | null | undefined)[]
  ): ICrbSourceOfInformationType[] {
    const crbSourceOfInformationTypes: ICrbSourceOfInformationType[] = crbSourceOfInformationTypesToCheck.filter(isPresent);
    if (crbSourceOfInformationTypes.length > 0) {
      const crbSourceOfInformationTypeCollectionIdentifiers = crbSourceOfInformationTypeCollection.map(
        crbSourceOfInformationTypeItem => getCrbSourceOfInformationTypeIdentifier(crbSourceOfInformationTypeItem)!
      );
      const crbSourceOfInformationTypesToAdd = crbSourceOfInformationTypes.filter(crbSourceOfInformationTypeItem => {
        const crbSourceOfInformationTypeIdentifier = getCrbSourceOfInformationTypeIdentifier(crbSourceOfInformationTypeItem);
        if (
          crbSourceOfInformationTypeIdentifier == null ||
          crbSourceOfInformationTypeCollectionIdentifiers.includes(crbSourceOfInformationTypeIdentifier)
        ) {
          return false;
        }
        crbSourceOfInformationTypeCollectionIdentifiers.push(crbSourceOfInformationTypeIdentifier);
        return true;
      });
      return [...crbSourceOfInformationTypesToAdd, ...crbSourceOfInformationTypeCollection];
    }
    return crbSourceOfInformationTypeCollection;
  }
}
