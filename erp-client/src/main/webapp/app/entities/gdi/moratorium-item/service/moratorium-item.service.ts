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
import { IMoratoriumItem, getMoratoriumItemIdentifier } from '../moratorium-item.model';

export type EntityResponseType = HttpResponse<IMoratoriumItem>;
export type EntityArrayResponseType = HttpResponse<IMoratoriumItem[]>;

@Injectable({ providedIn: 'root' })
export class MoratoriumItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/moratorium-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/moratorium-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moratoriumItem: IMoratoriumItem): Observable<EntityResponseType> {
    return this.http.post<IMoratoriumItem>(this.resourceUrl, moratoriumItem, { observe: 'response' });
  }

  update(moratoriumItem: IMoratoriumItem): Observable<EntityResponseType> {
    return this.http.put<IMoratoriumItem>(`${this.resourceUrl}/${getMoratoriumItemIdentifier(moratoriumItem) as number}`, moratoriumItem, {
      observe: 'response',
    });
  }

  partialUpdate(moratoriumItem: IMoratoriumItem): Observable<EntityResponseType> {
    return this.http.patch<IMoratoriumItem>(
      `${this.resourceUrl}/${getMoratoriumItemIdentifier(moratoriumItem) as number}`,
      moratoriumItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoratoriumItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoratoriumItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoratoriumItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addMoratoriumItemToCollectionIfMissing(
    moratoriumItemCollection: IMoratoriumItem[],
    ...moratoriumItemsToCheck: (IMoratoriumItem | null | undefined)[]
  ): IMoratoriumItem[] {
    const moratoriumItems: IMoratoriumItem[] = moratoriumItemsToCheck.filter(isPresent);
    if (moratoriumItems.length > 0) {
      const moratoriumItemCollectionIdentifiers = moratoriumItemCollection.map(
        moratoriumItemItem => getMoratoriumItemIdentifier(moratoriumItemItem)!
      );
      const moratoriumItemsToAdd = moratoriumItems.filter(moratoriumItemItem => {
        const moratoriumItemIdentifier = getMoratoriumItemIdentifier(moratoriumItemItem);
        if (moratoriumItemIdentifier == null || moratoriumItemCollectionIdentifiers.includes(moratoriumItemIdentifier)) {
          return false;
        }
        moratoriumItemCollectionIdentifiers.push(moratoriumItemIdentifier);
        return true;
      });
      return [...moratoriumItemsToAdd, ...moratoriumItemCollection];
    }
    return moratoriumItemCollection;
  }
}
