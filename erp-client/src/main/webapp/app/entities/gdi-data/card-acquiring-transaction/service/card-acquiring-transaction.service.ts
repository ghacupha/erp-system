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
import { ICardAcquiringTransaction, getCardAcquiringTransactionIdentifier } from '../card-acquiring-transaction.model';

export type EntityResponseType = HttpResponse<ICardAcquiringTransaction>;
export type EntityArrayResponseType = HttpResponse<ICardAcquiringTransaction[]>;

@Injectable({ providedIn: 'root' })
export class CardAcquiringTransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-acquiring-transactions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-acquiring-transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardAcquiringTransaction: ICardAcquiringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardAcquiringTransaction);
    return this.http
      .post<ICardAcquiringTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cardAcquiringTransaction: ICardAcquiringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardAcquiringTransaction);
    return this.http
      .put<ICardAcquiringTransaction>(
        `${this.resourceUrl}/${getCardAcquiringTransactionIdentifier(cardAcquiringTransaction) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cardAcquiringTransaction: ICardAcquiringTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cardAcquiringTransaction);
    return this.http
      .patch<ICardAcquiringTransaction>(
        `${this.resourceUrl}/${getCardAcquiringTransactionIdentifier(cardAcquiringTransaction) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICardAcquiringTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardAcquiringTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICardAcquiringTransaction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCardAcquiringTransactionToCollectionIfMissing(
    cardAcquiringTransactionCollection: ICardAcquiringTransaction[],
    ...cardAcquiringTransactionsToCheck: (ICardAcquiringTransaction | null | undefined)[]
  ): ICardAcquiringTransaction[] {
    const cardAcquiringTransactions: ICardAcquiringTransaction[] = cardAcquiringTransactionsToCheck.filter(isPresent);
    if (cardAcquiringTransactions.length > 0) {
      const cardAcquiringTransactionCollectionIdentifiers = cardAcquiringTransactionCollection.map(
        cardAcquiringTransactionItem => getCardAcquiringTransactionIdentifier(cardAcquiringTransactionItem)!
      );
      const cardAcquiringTransactionsToAdd = cardAcquiringTransactions.filter(cardAcquiringTransactionItem => {
        const cardAcquiringTransactionIdentifier = getCardAcquiringTransactionIdentifier(cardAcquiringTransactionItem);
        if (
          cardAcquiringTransactionIdentifier == null ||
          cardAcquiringTransactionCollectionIdentifiers.includes(cardAcquiringTransactionIdentifier)
        ) {
          return false;
        }
        cardAcquiringTransactionCollectionIdentifiers.push(cardAcquiringTransactionIdentifier);
        return true;
      });
      return [...cardAcquiringTransactionsToAdd, ...cardAcquiringTransactionCollection];
    }
    return cardAcquiringTransactionCollection;
  }

  protected convertDateFromClient(cardAcquiringTransaction: ICardAcquiringTransaction): ICardAcquiringTransaction {
    return Object.assign({}, cardAcquiringTransaction, {
      reportingDate: cardAcquiringTransaction.reportingDate?.isValid()
        ? cardAcquiringTransaction.reportingDate.format(DATE_FORMAT)
        : undefined,
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
      res.body.forEach((cardAcquiringTransaction: ICardAcquiringTransaction) => {
        cardAcquiringTransaction.reportingDate = cardAcquiringTransaction.reportingDate
          ? dayjs(cardAcquiringTransaction.reportingDate)
          : undefined;
      });
    }
    return res;
  }
}
