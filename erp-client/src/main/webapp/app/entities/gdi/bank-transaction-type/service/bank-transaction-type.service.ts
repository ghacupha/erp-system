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
import { IBankTransactionType, getBankTransactionTypeIdentifier } from '../bank-transaction-type.model';

export type EntityResponseType = HttpResponse<IBankTransactionType>;
export type EntityArrayResponseType = HttpResponse<IBankTransactionType[]>;

@Injectable({ providedIn: 'root' })
export class BankTransactionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-transaction-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/bank-transaction-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bankTransactionType: IBankTransactionType): Observable<EntityResponseType> {
    return this.http.post<IBankTransactionType>(this.resourceUrl, bankTransactionType, { observe: 'response' });
  }

  update(bankTransactionType: IBankTransactionType): Observable<EntityResponseType> {
    return this.http.put<IBankTransactionType>(
      `${this.resourceUrl}/${getBankTransactionTypeIdentifier(bankTransactionType) as number}`,
      bankTransactionType,
      { observe: 'response' }
    );
  }

  partialUpdate(bankTransactionType: IBankTransactionType): Observable<EntityResponseType> {
    return this.http.patch<IBankTransactionType>(
      `${this.resourceUrl}/${getBankTransactionTypeIdentifier(bankTransactionType) as number}`,
      bankTransactionType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBankTransactionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBankTransactionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBankTransactionType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addBankTransactionTypeToCollectionIfMissing(
    bankTransactionTypeCollection: IBankTransactionType[],
    ...bankTransactionTypesToCheck: (IBankTransactionType | null | undefined)[]
  ): IBankTransactionType[] {
    const bankTransactionTypes: IBankTransactionType[] = bankTransactionTypesToCheck.filter(isPresent);
    if (bankTransactionTypes.length > 0) {
      const bankTransactionTypeCollectionIdentifiers = bankTransactionTypeCollection.map(
        bankTransactionTypeItem => getBankTransactionTypeIdentifier(bankTransactionTypeItem)!
      );
      const bankTransactionTypesToAdd = bankTransactionTypes.filter(bankTransactionTypeItem => {
        const bankTransactionTypeIdentifier = getBankTransactionTypeIdentifier(bankTransactionTypeItem);
        if (bankTransactionTypeIdentifier == null || bankTransactionTypeCollectionIdentifiers.includes(bankTransactionTypeIdentifier)) {
          return false;
        }
        bankTransactionTypeCollectionIdentifiers.push(bankTransactionTypeIdentifier);
        return true;
      });
      return [...bankTransactionTypesToAdd, ...bankTransactionTypeCollection];
    }
    return bankTransactionTypeCollection;
  }
}
