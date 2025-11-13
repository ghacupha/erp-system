///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { ITransactionAccount, getTransactionAccountIdentifier } from '../transaction-account.model';

export type EntityResponseType = HttpResponse<ITransactionAccount>;
export type EntityArrayResponseType = HttpResponse<ITransactionAccount[]>;

@Injectable({ providedIn: 'root' })
export class TransactionAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaction-accounts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/transaction-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionAccount: ITransactionAccount): Observable<EntityResponseType> {
    return this.http.post<ITransactionAccount>(this.resourceUrl, transactionAccount, { observe: 'response' });
  }

  update(transactionAccount: ITransactionAccount): Observable<EntityResponseType> {
    return this.http.put<ITransactionAccount>(
      `${this.resourceUrl}/${getTransactionAccountIdentifier(transactionAccount) as number}`,
      transactionAccount,
      { observe: 'response' }
    );
  }

  partialUpdate(transactionAccount: ITransactionAccount): Observable<EntityResponseType> {
    return this.http.patch<ITransactionAccount>(
      `${this.resourceUrl}/${getTransactionAccountIdentifier(transactionAccount) as number}`,
      transactionAccount,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransactionAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccount[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTransactionAccountToCollectionIfMissing(
    transactionAccountCollection: ITransactionAccount[],
    ...transactionAccountsToCheck: (ITransactionAccount | null | undefined)[]
  ): ITransactionAccount[] {
    const transactionAccounts: ITransactionAccount[] = transactionAccountsToCheck.filter(isPresent);
    if (transactionAccounts.length > 0) {
      const transactionAccountCollectionIdentifiers = transactionAccountCollection.map(
        transactionAccountItem => getTransactionAccountIdentifier(transactionAccountItem)!
      );
      const transactionAccountsToAdd = transactionAccounts.filter(transactionAccountItem => {
        const transactionAccountIdentifier = getTransactionAccountIdentifier(transactionAccountItem);
        if (transactionAccountIdentifier == null || transactionAccountCollectionIdentifiers.includes(transactionAccountIdentifier)) {
          return false;
        }
        transactionAccountCollectionIdentifiers.push(transactionAccountIdentifier);
        return true;
      });
      return [...transactionAccountsToAdd, ...transactionAccountCollection];
    }
    return transactionAccountCollection;
  }
}
