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
import { ITransactionDetails, getTransactionDetailsIdentifier } from '../transaction-details.model';

export type EntityResponseType = HttpResponse<ITransactionDetails>;
export type EntityArrayResponseType = HttpResponse<ITransactionDetails[]>;

@Injectable({ providedIn: 'root' })
export class TransactionDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts/transaction-details');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounts/_search/transaction-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionDetails: ITransactionDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionDetails);
    return this.http
      .post<ITransactionDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(transactionDetails: ITransactionDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionDetails);
    return this.http
      .put<ITransactionDetails>(`${this.resourceUrl}/${getTransactionDetailsIdentifier(transactionDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(transactionDetails: ITransactionDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transactionDetails);
    return this.http
      .patch<ITransactionDetails>(`${this.resourceUrl}/${getTransactionDetailsIdentifier(transactionDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITransactionDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransactionDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransactionDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addTransactionDetailsToCollectionIfMissing(
    transactionDetailsCollection: ITransactionDetails[],
    ...transactionDetailsToCheck: (ITransactionDetails | null | undefined)[]
  ): ITransactionDetails[] {
    const transactionDetails: ITransactionDetails[] = transactionDetailsToCheck.filter(isPresent);
    if (transactionDetails.length > 0) {
      const transactionDetailsCollectionIdentifiers = transactionDetailsCollection.map(
        transactionDetailsItem => getTransactionDetailsIdentifier(transactionDetailsItem)!
      );
      const transactionDetailsToAdd = transactionDetails.filter(transactionDetailsItem => {
        const transactionDetailsIdentifier = getTransactionDetailsIdentifier(transactionDetailsItem);
        if (transactionDetailsIdentifier == null || transactionDetailsCollectionIdentifiers.includes(transactionDetailsIdentifier)) {
          return false;
        }
        transactionDetailsCollectionIdentifiers.push(transactionDetailsIdentifier);
        return true;
      });
      return [...transactionDetailsToAdd, ...transactionDetailsCollection];
    }
    return transactionDetailsCollection;
  }

  protected convertDateFromClient(transactionDetails: ITransactionDetails): ITransactionDetails {
    return Object.assign({}, transactionDetails, {
      transactionDate: transactionDetails.transactionDate?.isValid() ? transactionDetails.transactionDate.format(DATE_FORMAT) : undefined,
      createdAt: transactionDetails.createdAt?.isValid() ? transactionDetails.createdAt.toJSON() : undefined,
      modifiedAt: transactionDetails.modifiedAt?.isValid() ? transactionDetails.modifiedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate ? dayjs(res.body.transactionDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.modifiedAt = res.body.modifiedAt ? dayjs(res.body.modifiedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transactionDetails: ITransactionDetails) => {
        transactionDetails.transactionDate = transactionDetails.transactionDate ? dayjs(transactionDetails.transactionDate) : undefined;
        transactionDetails.createdAt = transactionDetails.createdAt ? dayjs(transactionDetails.createdAt) : undefined;
        transactionDetails.modifiedAt = transactionDetails.modifiedAt ? dayjs(transactionDetails.modifiedAt) : undefined;
      });
    }
    return res;
  }
}
