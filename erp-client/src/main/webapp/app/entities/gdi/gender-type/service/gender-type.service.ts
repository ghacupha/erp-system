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
import { IGenderType, getGenderTypeIdentifier } from '../gender-type.model';

export type EntityResponseType = HttpResponse<IGenderType>;
export type EntityArrayResponseType = HttpResponse<IGenderType[]>;

@Injectable({ providedIn: 'root' })
export class GenderTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gender-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/gender-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(genderType: IGenderType): Observable<EntityResponseType> {
    return this.http.post<IGenderType>(this.resourceUrl, genderType, { observe: 'response' });
  }

  update(genderType: IGenderType): Observable<EntityResponseType> {
    return this.http.put<IGenderType>(`${this.resourceUrl}/${getGenderTypeIdentifier(genderType) as number}`, genderType, {
      observe: 'response',
    });
  }

  partialUpdate(genderType: IGenderType): Observable<EntityResponseType> {
    return this.http.patch<IGenderType>(`${this.resourceUrl}/${getGenderTypeIdentifier(genderType) as number}`, genderType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGenderType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGenderType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGenderType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addGenderTypeToCollectionIfMissing(
    genderTypeCollection: IGenderType[],
    ...genderTypesToCheck: (IGenderType | null | undefined)[]
  ): IGenderType[] {
    const genderTypes: IGenderType[] = genderTypesToCheck.filter(isPresent);
    if (genderTypes.length > 0) {
      const genderTypeCollectionIdentifiers = genderTypeCollection.map(genderTypeItem => getGenderTypeIdentifier(genderTypeItem)!);
      const genderTypesToAdd = genderTypes.filter(genderTypeItem => {
        const genderTypeIdentifier = getGenderTypeIdentifier(genderTypeItem);
        if (genderTypeIdentifier == null || genderTypeCollectionIdentifiers.includes(genderTypeIdentifier)) {
          return false;
        }
        genderTypeCollectionIdentifiers.push(genderTypeIdentifier);
        return true;
      });
      return [...genderTypesToAdd, ...genderTypeCollection];
    }
    return genderTypeCollection;
  }
}
