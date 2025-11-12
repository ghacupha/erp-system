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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardFraudInformation, getCardFraudInformationIdentifier } from '../card-fraud-information.model';

export type EntityResponseType = HttpResponse<ICardFraudInformation>;
export type EntityArrayResponseType = HttpResponse<ICardFraudInformation[]>;

@Injectable({ providedIn: 'root' })
export class CardFraudInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-fraud-informations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-fraud-informations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardFraudInformation: ICardFraudInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardFraudInformation);
    return this.http
      .post<ICardFraudInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cardFraudInformation: ICardFraudInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardFraudInformation);
    return this.http
      .put<ICardFraudInformation>(`${this.resourceUrl}/${getCardFraudInformationIdentifier(cardFraudInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cardFraudInformation: ICardFraudInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardFraudInformation);
    return this.http
      .patch<ICardFraudInformation>(`${this.resourceUrl}/${getCardFraudInformationIdentifier(cardFraudInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICardFraudInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardFraudInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardFraudInformation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCardFraudInformationToCollectionIfMissing(
    cardFraudInformationCollection: ICardFraudInformation[],
    ...cardFraudInformationsToCheck: (ICardFraudInformation | null | undefined)[]
  ): ICardFraudInformation[] {
    const cardFraudInformations: ICardFraudInformation[] = cardFraudInformationsToCheck.filter(isPresent);
    if (cardFraudInformations.length > 0) {
      const cardFraudInformationCollectionIdentifiers = cardFraudInformationCollection.map(
        cardFraudInformationItem => getCardFraudInformationIdentifier(cardFraudInformationItem)!
      );
      const cardFraudInformationsToAdd = cardFraudInformations.filter(cardFraudInformationItem => {
        const cardFraudInformationIdentifier = getCardFraudInformationIdentifier(cardFraudInformationItem);
        if (cardFraudInformationIdentifier == null || cardFraudInformationCollectionIdentifiers.includes(cardFraudInformationIdentifier)) {
          return false;
        }
        cardFraudInformationCollectionIdentifiers.push(cardFraudInformationIdentifier);
        return true;
      });
      return [...cardFraudInformationsToAdd, ...cardFraudInformationCollection];
    }
    return cardFraudInformationCollection;
  }

  protected convertDateFromClient(cardFraudInformation: ICardFraudInformation): ICardFraudInformation {
    return Object.assign({}, cardFraudInformation, {
      reportingDate: cardFraudInformation.reportingDate?.isValid() ? cardFraudInformation.reportingDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cardFraudInformation: ICardFraudInformation) => {
        cardFraudInformation.reportingDate = cardFraudInformation.reportingDate ? dayjs(cardFraudInformation.reportingDate) : undefined;
      });
    }
    return res;
  }
}
