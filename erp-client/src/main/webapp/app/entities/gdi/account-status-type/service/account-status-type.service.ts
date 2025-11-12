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
import { IAccountStatusType, getAccountStatusTypeIdentifier } from '../account-status-type.model';

export type EntityResponseType = HttpResponse<IAccountStatusType>;
export type EntityArrayResponseType = HttpResponse<IAccountStatusType[]>;

@Injectable({ providedIn: 'root' })
export class AccountStatusTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-status-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/account-status-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountStatusType: IAccountStatusType): Observable<EntityResponseType> {
    return this.http.post<IAccountStatusType>(this.resourceUrl, accountStatusType, { observe: 'response' });
  }

  update(accountStatusType: IAccountStatusType): Observable<EntityResponseType> {
    return this.http.put<IAccountStatusType>(
      `${this.resourceUrl}/${getAccountStatusTypeIdentifier(accountStatusType) as number}`,
      accountStatusType,
      { observe: 'response' }
    );
  }

  partialUpdate(accountStatusType: IAccountStatusType): Observable<EntityResponseType> {
    return this.http.patch<IAccountStatusType>(
      `${this.resourceUrl}/${getAccountStatusTypeIdentifier(accountStatusType) as number}`,
      accountStatusType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountStatusType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountStatusType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountStatusType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAccountStatusTypeToCollectionIfMissing(
    accountStatusTypeCollection: IAccountStatusType[],
    ...accountStatusTypesToCheck: (IAccountStatusType | null | undefined)[]
  ): IAccountStatusType[] {
    const accountStatusTypes: IAccountStatusType[] = accountStatusTypesToCheck.filter(isPresent);
    if (accountStatusTypes.length > 0) {
      const accountStatusTypeCollectionIdentifiers = accountStatusTypeCollection.map(
        accountStatusTypeItem => getAccountStatusTypeIdentifier(accountStatusTypeItem)!
      );
      const accountStatusTypesToAdd = accountStatusTypes.filter(accountStatusTypeItem => {
        const accountStatusTypeIdentifier = getAccountStatusTypeIdentifier(accountStatusTypeItem);
        if (accountStatusTypeIdentifier == null || accountStatusTypeCollectionIdentifiers.includes(accountStatusTypeIdentifier)) {
          return false;
        }
        accountStatusTypeCollectionIdentifiers.push(accountStatusTypeIdentifier);
        return true;
      });
      return [...accountStatusTypesToAdd, ...accountStatusTypeCollection];
    }
    return accountStatusTypeCollection;
  }
}
