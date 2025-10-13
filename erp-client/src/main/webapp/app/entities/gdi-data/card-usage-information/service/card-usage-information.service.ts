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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardUsageInformation, getCardUsageInformationIdentifier } from '../card-usage-information.model';

export type EntityResponseType = HttpResponse<ICardUsageInformation>;
export type EntityArrayResponseType = HttpResponse<ICardUsageInformation[]>;

@Injectable({ providedIn: 'root' })
export class CardUsageInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-usage-informations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-usage-informations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardUsageInformation: ICardUsageInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardUsageInformation);
    return this.http
      .post<ICardUsageInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cardUsageInformation: ICardUsageInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardUsageInformation);
    return this.http
      .put<ICardUsageInformation>(`${this.resourceUrl}/${getCardUsageInformationIdentifier(cardUsageInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cardUsageInformation: ICardUsageInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardUsageInformation);
    return this.http
      .patch<ICardUsageInformation>(`${this.resourceUrl}/${getCardUsageInformationIdentifier(cardUsageInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICardUsageInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardUsageInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardUsageInformation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCardUsageInformationToCollectionIfMissing(
    cardUsageInformationCollection: ICardUsageInformation[],
    ...cardUsageInformationsToCheck: (ICardUsageInformation | null | undefined)[]
  ): ICardUsageInformation[] {
    const cardUsageInformations: ICardUsageInformation[] = cardUsageInformationsToCheck.filter(isPresent);
    if (cardUsageInformations.length > 0) {
      const cardUsageInformationCollectionIdentifiers = cardUsageInformationCollection.map(
        cardUsageInformationItem => getCardUsageInformationIdentifier(cardUsageInformationItem)!
      );
      const cardUsageInformationsToAdd = cardUsageInformations.filter(cardUsageInformationItem => {
        const cardUsageInformationIdentifier = getCardUsageInformationIdentifier(cardUsageInformationItem);
        if (cardUsageInformationIdentifier == null || cardUsageInformationCollectionIdentifiers.includes(cardUsageInformationIdentifier)) {
          return false;
        }
        cardUsageInformationCollectionIdentifiers.push(cardUsageInformationIdentifier);
        return true;
      });
      return [...cardUsageInformationsToAdd, ...cardUsageInformationCollection];
    }
    return cardUsageInformationCollection;
  }

  protected convertDateFromClient(cardUsageInformation: ICardUsageInformation): ICardUsageInformation {
    return Object.assign({}, cardUsageInformation, {
      reportingDate: cardUsageInformation.reportingDate?.isValid() ? cardUsageInformation.reportingDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((cardUsageInformation: ICardUsageInformation) => {
        cardUsageInformation.reportingDate = cardUsageInformation.reportingDate ? dayjs(cardUsageInformation.reportingDate) : undefined;
      });
    }
    return res;
  }
}
