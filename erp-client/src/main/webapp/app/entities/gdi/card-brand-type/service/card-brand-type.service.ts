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
import { ICardBrandType, getCardBrandTypeIdentifier } from '../card-brand-type.model';

export type EntityResponseType = HttpResponse<ICardBrandType>;
export type EntityArrayResponseType = HttpResponse<ICardBrandType[]>;

@Injectable({ providedIn: 'root' })
export class CardBrandTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-brand-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-brand-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardBrandType: ICardBrandType): Observable<EntityResponseType> {
    return this.http.post<ICardBrandType>(this.resourceUrl, cardBrandType, { observe: 'response' });
  }

  update(cardBrandType: ICardBrandType): Observable<EntityResponseType> {
    return this.http.put<ICardBrandType>(`${this.resourceUrl}/${getCardBrandTypeIdentifier(cardBrandType) as number}`, cardBrandType, {
      observe: 'response',
    });
  }

  partialUpdate(cardBrandType: ICardBrandType): Observable<EntityResponseType> {
    return this.http.patch<ICardBrandType>(`${this.resourceUrl}/${getCardBrandTypeIdentifier(cardBrandType) as number}`, cardBrandType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardBrandType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardBrandType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardBrandType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardBrandTypeToCollectionIfMissing(
    cardBrandTypeCollection: ICardBrandType[],
    ...cardBrandTypesToCheck: (ICardBrandType | null | undefined)[]
  ): ICardBrandType[] {
    const cardBrandTypes: ICardBrandType[] = cardBrandTypesToCheck.filter(isPresent);
    if (cardBrandTypes.length > 0) {
      const cardBrandTypeCollectionIdentifiers = cardBrandTypeCollection.map(
        cardBrandTypeItem => getCardBrandTypeIdentifier(cardBrandTypeItem)!
      );
      const cardBrandTypesToAdd = cardBrandTypes.filter(cardBrandTypeItem => {
        const cardBrandTypeIdentifier = getCardBrandTypeIdentifier(cardBrandTypeItem);
        if (cardBrandTypeIdentifier == null || cardBrandTypeCollectionIdentifiers.includes(cardBrandTypeIdentifier)) {
          return false;
        }
        cardBrandTypeCollectionIdentifiers.push(cardBrandTypeIdentifier);
        return true;
      });
      return [...cardBrandTypesToAdd, ...cardBrandTypeCollection];
    }
    return cardBrandTypeCollection;
  }
}
