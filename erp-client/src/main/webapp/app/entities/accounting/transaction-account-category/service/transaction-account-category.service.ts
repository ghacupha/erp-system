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
import { ITransactionAccountCategory, getTransactionAccountCategoryIdentifier } from '../transaction-account-category.model';

export type EntityResponseType = HttpResponse<ITransactionAccountCategory>;
export type EntityArrayResponseType = HttpResponse<ITransactionAccountCategory[]>;

@Injectable({ providedIn: 'root' })
export class TransactionAccountCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaction-account-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/transaction-account-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transactionAccountCategory: ITransactionAccountCategory): Observable<EntityResponseType> {
    return this.http.post<ITransactionAccountCategory>(this.resourceUrl, transactionAccountCategory, { observe: 'response' });
  }

  update(transactionAccountCategory: ITransactionAccountCategory): Observable<EntityResponseType> {
    return this.http.put<ITransactionAccountCategory>(
      `${this.resourceUrl}/${getTransactionAccountCategoryIdentifier(transactionAccountCategory) as number}`,
      transactionAccountCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(transactionAccountCategory: ITransactionAccountCategory): Observable<EntityResponseType> {
    return this.http.patch<ITransactionAccountCategory>(
      `${this.resourceUrl}/${getTransactionAccountCategoryIdentifier(transactionAccountCategory) as number}`,
      transactionAccountCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransactionAccountCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccountCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransactionAccountCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTransactionAccountCategoryToCollectionIfMissing(
    transactionAccountCategoryCollection: ITransactionAccountCategory[],
    ...transactionAccountCategoriesToCheck: (ITransactionAccountCategory | null | undefined)[]
  ): ITransactionAccountCategory[] {
    const transactionAccountCategories: ITransactionAccountCategory[] = transactionAccountCategoriesToCheck.filter(isPresent);
    if (transactionAccountCategories.length > 0) {
      const transactionAccountCategoryCollectionIdentifiers = transactionAccountCategoryCollection.map(
        transactionAccountCategoryItem => getTransactionAccountCategoryIdentifier(transactionAccountCategoryItem)!
      );
      const transactionAccountCategoriesToAdd = transactionAccountCategories.filter(transactionAccountCategoryItem => {
        const transactionAccountCategoryIdentifier = getTransactionAccountCategoryIdentifier(transactionAccountCategoryItem);
        if (
          transactionAccountCategoryIdentifier == null ||
          transactionAccountCategoryCollectionIdentifiers.includes(transactionAccountCategoryIdentifier)
        ) {
          return false;
        }
        transactionAccountCategoryCollectionIdentifiers.push(transactionAccountCategoryIdentifier);
        return true;
      });
      return [...transactionAccountCategoriesToAdd, ...transactionAccountCategoryCollection];
    }
    return transactionAccountCategoryCollection;
  }
}
