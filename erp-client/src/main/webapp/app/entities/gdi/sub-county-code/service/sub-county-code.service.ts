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
import { ISubCountyCode, getSubCountyCodeIdentifier } from '../sub-county-code.model';

export type EntityResponseType = HttpResponse<ISubCountyCode>;
export type EntityArrayResponseType = HttpResponse<ISubCountyCode[]>;

@Injectable({ providedIn: 'root' })
export class SubCountyCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-county-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/sub-county-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subCountyCode: ISubCountyCode): Observable<EntityResponseType> {
    return this.http.post<ISubCountyCode>(this.resourceUrl, subCountyCode, { observe: 'response' });
  }

  update(subCountyCode: ISubCountyCode): Observable<EntityResponseType> {
    return this.http.put<ISubCountyCode>(`${this.resourceUrl}/${getSubCountyCodeIdentifier(subCountyCode) as number}`, subCountyCode, {
      observe: 'response',
    });
  }

  partialUpdate(subCountyCode: ISubCountyCode): Observable<EntityResponseType> {
    return this.http.patch<ISubCountyCode>(`${this.resourceUrl}/${getSubCountyCodeIdentifier(subCountyCode) as number}`, subCountyCode, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubCountyCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubCountyCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubCountyCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSubCountyCodeToCollectionIfMissing(
    subCountyCodeCollection: ISubCountyCode[],
    ...subCountyCodesToCheck: (ISubCountyCode | null | undefined)[]
  ): ISubCountyCode[] {
    const subCountyCodes: ISubCountyCode[] = subCountyCodesToCheck.filter(isPresent);
    if (subCountyCodes.length > 0) {
      const subCountyCodeCollectionIdentifiers = subCountyCodeCollection.map(
        subCountyCodeItem => getSubCountyCodeIdentifier(subCountyCodeItem)!
      );
      const subCountyCodesToAdd = subCountyCodes.filter(subCountyCodeItem => {
        const subCountyCodeIdentifier = getSubCountyCodeIdentifier(subCountyCodeItem);
        if (subCountyCodeIdentifier == null || subCountyCodeCollectionIdentifiers.includes(subCountyCodeIdentifier)) {
          return false;
        }
        subCountyCodeCollectionIdentifiers.push(subCountyCodeIdentifier);
        return true;
      });
      return [...subCountyCodesToAdd, ...subCountyCodeCollection];
    }
    return subCountyCodeCollection;
  }
}
