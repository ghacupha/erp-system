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
import { ICardStatusFlag, getCardStatusFlagIdentifier } from '../card-status-flag.model';

export type EntityResponseType = HttpResponse<ICardStatusFlag>;
export type EntityArrayResponseType = HttpResponse<ICardStatusFlag[]>;

@Injectable({ providedIn: 'root' })
export class CardStatusFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-status-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-status-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardStatusFlag: ICardStatusFlag): Observable<EntityResponseType> {
    return this.http.post<ICardStatusFlag>(this.resourceUrl, cardStatusFlag, { observe: 'response' });
  }

  update(cardStatusFlag: ICardStatusFlag): Observable<EntityResponseType> {
    return this.http.put<ICardStatusFlag>(`${this.resourceUrl}/${getCardStatusFlagIdentifier(cardStatusFlag) as number}`, cardStatusFlag, {
      observe: 'response',
    });
  }

  partialUpdate(cardStatusFlag: ICardStatusFlag): Observable<EntityResponseType> {
    return this.http.patch<ICardStatusFlag>(
      `${this.resourceUrl}/${getCardStatusFlagIdentifier(cardStatusFlag) as number}`,
      cardStatusFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardStatusFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardStatusFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardStatusFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardStatusFlagToCollectionIfMissing(
    cardStatusFlagCollection: ICardStatusFlag[],
    ...cardStatusFlagsToCheck: (ICardStatusFlag | null | undefined)[]
  ): ICardStatusFlag[] {
    const cardStatusFlags: ICardStatusFlag[] = cardStatusFlagsToCheck.filter(isPresent);
    if (cardStatusFlags.length > 0) {
      const cardStatusFlagCollectionIdentifiers = cardStatusFlagCollection.map(
        cardStatusFlagItem => getCardStatusFlagIdentifier(cardStatusFlagItem)!
      );
      const cardStatusFlagsToAdd = cardStatusFlags.filter(cardStatusFlagItem => {
        const cardStatusFlagIdentifier = getCardStatusFlagIdentifier(cardStatusFlagItem);
        if (cardStatusFlagIdentifier == null || cardStatusFlagCollectionIdentifiers.includes(cardStatusFlagIdentifier)) {
          return false;
        }
        cardStatusFlagCollectionIdentifiers.push(cardStatusFlagIdentifier);
        return true;
      });
      return [...cardStatusFlagsToAdd, ...cardStatusFlagCollection];
    }
    return cardStatusFlagCollection;
  }
}
