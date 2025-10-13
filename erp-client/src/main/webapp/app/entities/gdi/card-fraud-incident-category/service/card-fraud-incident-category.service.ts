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
import { ICardFraudIncidentCategory, getCardFraudIncidentCategoryIdentifier } from '../card-fraud-incident-category.model';

export type EntityResponseType = HttpResponse<ICardFraudIncidentCategory>;
export type EntityArrayResponseType = HttpResponse<ICardFraudIncidentCategory[]>;

@Injectable({ providedIn: 'root' })
export class CardFraudIncidentCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-fraud-incident-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-fraud-incident-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardFraudIncidentCategory: ICardFraudIncidentCategory): Observable<EntityResponseType> {
    return this.http.post<ICardFraudIncidentCategory>(this.resourceUrl, cardFraudIncidentCategory, { observe: 'response' });
  }

  update(cardFraudIncidentCategory: ICardFraudIncidentCategory): Observable<EntityResponseType> {
    return this.http.put<ICardFraudIncidentCategory>(
      `${this.resourceUrl}/${getCardFraudIncidentCategoryIdentifier(cardFraudIncidentCategory) as number}`,
      cardFraudIncidentCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(cardFraudIncidentCategory: ICardFraudIncidentCategory): Observable<EntityResponseType> {
    return this.http.patch<ICardFraudIncidentCategory>(
      `${this.resourceUrl}/${getCardFraudIncidentCategoryIdentifier(cardFraudIncidentCategory) as number}`,
      cardFraudIncidentCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardFraudIncidentCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardFraudIncidentCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardFraudIncidentCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardFraudIncidentCategoryToCollectionIfMissing(
    cardFraudIncidentCategoryCollection: ICardFraudIncidentCategory[],
    ...cardFraudIncidentCategoriesToCheck: (ICardFraudIncidentCategory | null | undefined)[]
  ): ICardFraudIncidentCategory[] {
    const cardFraudIncidentCategories: ICardFraudIncidentCategory[] = cardFraudIncidentCategoriesToCheck.filter(isPresent);
    if (cardFraudIncidentCategories.length > 0) {
      const cardFraudIncidentCategoryCollectionIdentifiers = cardFraudIncidentCategoryCollection.map(
        cardFraudIncidentCategoryItem => getCardFraudIncidentCategoryIdentifier(cardFraudIncidentCategoryItem)!
      );
      const cardFraudIncidentCategoriesToAdd = cardFraudIncidentCategories.filter(cardFraudIncidentCategoryItem => {
        const cardFraudIncidentCategoryIdentifier = getCardFraudIncidentCategoryIdentifier(cardFraudIncidentCategoryItem);
        if (
          cardFraudIncidentCategoryIdentifier == null ||
          cardFraudIncidentCategoryCollectionIdentifiers.includes(cardFraudIncidentCategoryIdentifier)
        ) {
          return false;
        }
        cardFraudIncidentCategoryCollectionIdentifiers.push(cardFraudIncidentCategoryIdentifier);
        return true;
      });
      return [...cardFraudIncidentCategoriesToAdd, ...cardFraudIncidentCategoryCollection];
    }
    return cardFraudIncidentCategoryCollection;
  }
}
