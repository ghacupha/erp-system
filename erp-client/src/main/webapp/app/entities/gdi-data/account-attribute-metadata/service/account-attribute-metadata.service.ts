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
import { IAccountAttributeMetadata, getAccountAttributeMetadataIdentifier } from '../account-attribute-metadata.model';

export type EntityResponseType = HttpResponse<IAccountAttributeMetadata>;
export type EntityArrayResponseType = HttpResponse<IAccountAttributeMetadata[]>;

@Injectable({ providedIn: 'root' })
export class AccountAttributeMetadataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-attribute-metadata');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/account-attribute-metadata');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountAttributeMetadata: IAccountAttributeMetadata): Observable<EntityResponseType> {
    return this.http.post<IAccountAttributeMetadata>(this.resourceUrl, accountAttributeMetadata, { observe: 'response' });
  }

  update(accountAttributeMetadata: IAccountAttributeMetadata): Observable<EntityResponseType> {
    return this.http.put<IAccountAttributeMetadata>(
      `${this.resourceUrl}/${getAccountAttributeMetadataIdentifier(accountAttributeMetadata) as number}`,
      accountAttributeMetadata,
      { observe: 'response' }
    );
  }

  partialUpdate(accountAttributeMetadata: IAccountAttributeMetadata): Observable<EntityResponseType> {
    return this.http.patch<IAccountAttributeMetadata>(
      `${this.resourceUrl}/${getAccountAttributeMetadataIdentifier(accountAttributeMetadata) as number}`,
      accountAttributeMetadata,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountAttributeMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountAttributeMetadata[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountAttributeMetadata[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAccountAttributeMetadataToCollectionIfMissing(
    accountAttributeMetadataCollection: IAccountAttributeMetadata[],
    ...accountAttributeMetadataToCheck: (IAccountAttributeMetadata | null | undefined)[]
  ): IAccountAttributeMetadata[] {
    const accountAttributeMetadata: IAccountAttributeMetadata[] = accountAttributeMetadataToCheck.filter(isPresent);
    if (accountAttributeMetadata.length > 0) {
      const accountAttributeMetadataCollectionIdentifiers = accountAttributeMetadataCollection.map(
        accountAttributeMetadataItem => getAccountAttributeMetadataIdentifier(accountAttributeMetadataItem)!
      );
      const accountAttributeMetadataToAdd = accountAttributeMetadata.filter(accountAttributeMetadataItem => {
        const accountAttributeMetadataIdentifier = getAccountAttributeMetadataIdentifier(accountAttributeMetadataItem);
        if (
          accountAttributeMetadataIdentifier == null ||
          accountAttributeMetadataCollectionIdentifiers.includes(accountAttributeMetadataIdentifier)
        ) {
          return false;
        }
        accountAttributeMetadataCollectionIdentifiers.push(accountAttributeMetadataIdentifier);
        return true;
      });
      return [...accountAttributeMetadataToAdd, ...accountAttributeMetadataCollection];
    }
    return accountAttributeMetadataCollection;
  }
}
