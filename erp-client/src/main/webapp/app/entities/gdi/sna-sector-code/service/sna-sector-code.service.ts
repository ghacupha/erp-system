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
import { ISnaSectorCode, getSnaSectorCodeIdentifier } from '../sna-sector-code.model';

export type EntityResponseType = HttpResponse<ISnaSectorCode>;
export type EntityArrayResponseType = HttpResponse<ISnaSectorCode[]>;

@Injectable({ providedIn: 'root' })
export class SnaSectorCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sna-sector-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/sna-sector-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(snaSectorCode: ISnaSectorCode): Observable<EntityResponseType> {
    return this.http.post<ISnaSectorCode>(this.resourceUrl, snaSectorCode, { observe: 'response' });
  }

  update(snaSectorCode: ISnaSectorCode): Observable<EntityResponseType> {
    return this.http.put<ISnaSectorCode>(`${this.resourceUrl}/${getSnaSectorCodeIdentifier(snaSectorCode) as number}`, snaSectorCode, {
      observe: 'response',
    });
  }

  partialUpdate(snaSectorCode: ISnaSectorCode): Observable<EntityResponseType> {
    return this.http.patch<ISnaSectorCode>(`${this.resourceUrl}/${getSnaSectorCodeIdentifier(snaSectorCode) as number}`, snaSectorCode, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISnaSectorCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISnaSectorCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISnaSectorCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSnaSectorCodeToCollectionIfMissing(
    snaSectorCodeCollection: ISnaSectorCode[],
    ...snaSectorCodesToCheck: (ISnaSectorCode | null | undefined)[]
  ): ISnaSectorCode[] {
    const snaSectorCodes: ISnaSectorCode[] = snaSectorCodesToCheck.filter(isPresent);
    if (snaSectorCodes.length > 0) {
      const snaSectorCodeCollectionIdentifiers = snaSectorCodeCollection.map(
        snaSectorCodeItem => getSnaSectorCodeIdentifier(snaSectorCodeItem)!
      );
      const snaSectorCodesToAdd = snaSectorCodes.filter(snaSectorCodeItem => {
        const snaSectorCodeIdentifier = getSnaSectorCodeIdentifier(snaSectorCodeItem);
        if (snaSectorCodeIdentifier == null || snaSectorCodeCollectionIdentifiers.includes(snaSectorCodeIdentifier)) {
          return false;
        }
        snaSectorCodeCollectionIdentifiers.push(snaSectorCodeIdentifier);
        return true;
      });
      return [...snaSectorCodesToAdd, ...snaSectorCodeCollection];
    }
    return snaSectorCodeCollection;
  }
}
