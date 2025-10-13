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
import { IGdiTransactionDataIndex, getGdiTransactionDataIndexIdentifier } from '../gdi-transaction-data-index.model';

export type EntityResponseType = HttpResponse<IGdiTransactionDataIndex>;
export type EntityArrayResponseType = HttpResponse<IGdiTransactionDataIndex[]>;

@Injectable({ providedIn: 'root' })
export class GdiTransactionDataIndexService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gdi-transaction-data-indices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/gdi-transaction-data-indices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gdiTransactionDataIndex: IGdiTransactionDataIndex): Observable<EntityResponseType> {
    return this.http.post<IGdiTransactionDataIndex>(this.resourceUrl, gdiTransactionDataIndex, { observe: 'response' });
  }

  update(gdiTransactionDataIndex: IGdiTransactionDataIndex): Observable<EntityResponseType> {
    return this.http.put<IGdiTransactionDataIndex>(
      `${this.resourceUrl}/${getGdiTransactionDataIndexIdentifier(gdiTransactionDataIndex) as number}`,
      gdiTransactionDataIndex,
      { observe: 'response' }
    );
  }

  partialUpdate(gdiTransactionDataIndex: IGdiTransactionDataIndex): Observable<EntityResponseType> {
    return this.http.patch<IGdiTransactionDataIndex>(
      `${this.resourceUrl}/${getGdiTransactionDataIndexIdentifier(gdiTransactionDataIndex) as number}`,
      gdiTransactionDataIndex,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGdiTransactionDataIndex>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGdiTransactionDataIndex[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGdiTransactionDataIndex[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addGdiTransactionDataIndexToCollectionIfMissing(
    gdiTransactionDataIndexCollection: IGdiTransactionDataIndex[],
    ...gdiTransactionDataIndicesToCheck: (IGdiTransactionDataIndex | null | undefined)[]
  ): IGdiTransactionDataIndex[] {
    const gdiTransactionDataIndices: IGdiTransactionDataIndex[] = gdiTransactionDataIndicesToCheck.filter(isPresent);
    if (gdiTransactionDataIndices.length > 0) {
      const gdiTransactionDataIndexCollectionIdentifiers = gdiTransactionDataIndexCollection.map(
        gdiTransactionDataIndexItem => getGdiTransactionDataIndexIdentifier(gdiTransactionDataIndexItem)!
      );
      const gdiTransactionDataIndicesToAdd = gdiTransactionDataIndices.filter(gdiTransactionDataIndexItem => {
        const gdiTransactionDataIndexIdentifier = getGdiTransactionDataIndexIdentifier(gdiTransactionDataIndexItem);
        if (
          gdiTransactionDataIndexIdentifier == null ||
          gdiTransactionDataIndexCollectionIdentifiers.includes(gdiTransactionDataIndexIdentifier)
        ) {
          return false;
        }
        gdiTransactionDataIndexCollectionIdentifiers.push(gdiTransactionDataIndexIdentifier);
        return true;
      });
      return [...gdiTransactionDataIndicesToAdd, ...gdiTransactionDataIndexCollection];
    }
    return gdiTransactionDataIndexCollection;
  }
}
