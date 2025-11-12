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
import {
  ITransactionAccountPostingProcessType,
  getTransactionAccountPostingProcessTypeIdentifier,
} from '../transaction-account-posting-process-type.model';

export type EntityResponseType = HttpResponse<ITransactionAccountPostingProcessType>;
export type EntityArrayResponseType = HttpResponse<ITransactionAccountPostingProcessType[]>;

@Injectable({ providedIn: 'root' })
export class TransactionAccountPostingProcessTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts/transaction-account-posting-process-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounts/_search/transaction-account-posting-process-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionAccountPostingProcessType: ITransactionAccountPostingProcessType): Observable<EntityResponseType> {
    return this.http.post<ITransactionAccountPostingProcessType>(this.resourceUrl, transactionAccountPostingProcessType, {
      observe: 'response',
    });
  }

  update(transactionAccountPostingProcessType: ITransactionAccountPostingProcessType): Observable<EntityResponseType> {
    return this.http.put<ITransactionAccountPostingProcessType>(
      `${this.resourceUrl}/${getTransactionAccountPostingProcessTypeIdentifier(transactionAccountPostingProcessType) as number}`,
      transactionAccountPostingProcessType,
      { observe: 'response' }
    );
  }

  partialUpdate(transactionAccountPostingProcessType: ITransactionAccountPostingProcessType): Observable<EntityResponseType> {
    return this.http.patch<ITransactionAccountPostingProcessType>(
      `${this.resourceUrl}/${getTransactionAccountPostingProcessTypeIdentifier(transactionAccountPostingProcessType) as number}`,
      transactionAccountPostingProcessType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransactionAccountPostingProcessType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccountPostingProcessType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccountPostingProcessType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTransactionAccountPostingProcessTypeToCollectionIfMissing(
    transactionAccountPostingProcessTypeCollection: ITransactionAccountPostingProcessType[],
    ...transactionAccountPostingProcessTypesToCheck: (ITransactionAccountPostingProcessType | null | undefined)[]
  ): ITransactionAccountPostingProcessType[] {
    const transactionAccountPostingProcessTypes: ITransactionAccountPostingProcessType[] =
      transactionAccountPostingProcessTypesToCheck.filter(isPresent);
    if (transactionAccountPostingProcessTypes.length > 0) {
      const transactionAccountPostingProcessTypeCollectionIdentifiers = transactionAccountPostingProcessTypeCollection.map(
        transactionAccountPostingProcessTypeItem =>
          getTransactionAccountPostingProcessTypeIdentifier(transactionAccountPostingProcessTypeItem)!
      );
      const transactionAccountPostingProcessTypesToAdd = transactionAccountPostingProcessTypes.filter(
        transactionAccountPostingProcessTypeItem => {
          const transactionAccountPostingProcessTypeIdentifier = getTransactionAccountPostingProcessTypeIdentifier(
            transactionAccountPostingProcessTypeItem
          );
          if (
            transactionAccountPostingProcessTypeIdentifier == null ||
            transactionAccountPostingProcessTypeCollectionIdentifiers.includes(transactionAccountPostingProcessTypeIdentifier)
          ) {
            return false;
          }
          transactionAccountPostingProcessTypeCollectionIdentifiers.push(transactionAccountPostingProcessTypeIdentifier);
          return true;
        }
      );
      return [...transactionAccountPostingProcessTypesToAdd, ...transactionAccountPostingProcessTypeCollection];
    }
    return transactionAccountPostingProcessTypeCollection;
  }
}
