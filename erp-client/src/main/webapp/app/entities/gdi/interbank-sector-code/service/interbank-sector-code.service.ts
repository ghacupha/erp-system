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
import { IInterbankSectorCode, getInterbankSectorCodeIdentifier } from '../interbank-sector-code.model';

export type EntityResponseType = HttpResponse<IInterbankSectorCode>;
export type EntityArrayResponseType = HttpResponse<IInterbankSectorCode[]>;

@Injectable({ providedIn: 'root' })
export class InterbankSectorCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/interbank-sector-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/interbank-sector-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(interbankSectorCode: IInterbankSectorCode): Observable<EntityResponseType> {
    return this.http.post<IInterbankSectorCode>(this.resourceUrl, interbankSectorCode, { observe: 'response' });
  }

  update(interbankSectorCode: IInterbankSectorCode): Observable<EntityResponseType> {
    return this.http.put<IInterbankSectorCode>(
      `${this.resourceUrl}/${getInterbankSectorCodeIdentifier(interbankSectorCode) as number}`,
      interbankSectorCode,
      { observe: 'response' }
    );
  }

  partialUpdate(interbankSectorCode: IInterbankSectorCode): Observable<EntityResponseType> {
    return this.http.patch<IInterbankSectorCode>(
      `${this.resourceUrl}/${getInterbankSectorCodeIdentifier(interbankSectorCode) as number}`,
      interbankSectorCode,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterbankSectorCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterbankSectorCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterbankSectorCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addInterbankSectorCodeToCollectionIfMissing(
    interbankSectorCodeCollection: IInterbankSectorCode[],
    ...interbankSectorCodesToCheck: (IInterbankSectorCode | null | undefined)[]
  ): IInterbankSectorCode[] {
    const interbankSectorCodes: IInterbankSectorCode[] = interbankSectorCodesToCheck.filter(isPresent);
    if (interbankSectorCodes.length > 0) {
      const interbankSectorCodeCollectionIdentifiers = interbankSectorCodeCollection.map(
        interbankSectorCodeItem => getInterbankSectorCodeIdentifier(interbankSectorCodeItem)!
      );
      const interbankSectorCodesToAdd = interbankSectorCodes.filter(interbankSectorCodeItem => {
        const interbankSectorCodeIdentifier = getInterbankSectorCodeIdentifier(interbankSectorCodeItem);
        if (interbankSectorCodeIdentifier == null || interbankSectorCodeCollectionIdentifiers.includes(interbankSectorCodeIdentifier)) {
          return false;
        }
        interbankSectorCodeCollectionIdentifiers.push(interbankSectorCodeIdentifier);
        return true;
      });
      return [...interbankSectorCodesToAdd, ...interbankSectorCodeCollection];
    }
    return interbankSectorCodeCollection;
  }
}
