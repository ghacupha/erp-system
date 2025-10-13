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
import { IFxTransactionType, getFxTransactionTypeIdentifier } from '../fx-transaction-type.model';

export type EntityResponseType = HttpResponse<IFxTransactionType>;
export type EntityArrayResponseType = HttpResponse<IFxTransactionType[]>;

@Injectable({ providedIn: 'root' })
export class FxTransactionTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-transaction-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-transaction-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxTransactionType: IFxTransactionType): Observable<EntityResponseType> {
    return this.http.post<IFxTransactionType>(this.resourceUrl, fxTransactionType, { observe: 'response' });
  }

  update(fxTransactionType: IFxTransactionType): Observable<EntityResponseType> {
    return this.http.put<IFxTransactionType>(
      `${this.resourceUrl}/${getFxTransactionTypeIdentifier(fxTransactionType) as number}`,
      fxTransactionType,
      { observe: 'response' }
    );
  }

  partialUpdate(fxTransactionType: IFxTransactionType): Observable<EntityResponseType> {
    return this.http.patch<IFxTransactionType>(
      `${this.resourceUrl}/${getFxTransactionTypeIdentifier(fxTransactionType) as number}`,
      fxTransactionType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxTransactionType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxTransactionTypeToCollectionIfMissing(
    fxTransactionTypeCollection: IFxTransactionType[],
    ...fxTransactionTypesToCheck: (IFxTransactionType | null | undefined)[]
  ): IFxTransactionType[] {
    const fxTransactionTypes: IFxTransactionType[] = fxTransactionTypesToCheck.filter(isPresent);
    if (fxTransactionTypes.length > 0) {
      const fxTransactionTypeCollectionIdentifiers = fxTransactionTypeCollection.map(
        fxTransactionTypeItem => getFxTransactionTypeIdentifier(fxTransactionTypeItem)!
      );
      const fxTransactionTypesToAdd = fxTransactionTypes.filter(fxTransactionTypeItem => {
        const fxTransactionTypeIdentifier = getFxTransactionTypeIdentifier(fxTransactionTypeItem);
        if (fxTransactionTypeIdentifier == null || fxTransactionTypeCollectionIdentifiers.includes(fxTransactionTypeIdentifier)) {
          return false;
        }
        fxTransactionTypeCollectionIdentifiers.push(fxTransactionTypeIdentifier);
        return true;
      });
      return [...fxTransactionTypesToAdd, ...fxTransactionTypeCollection];
    }
    return fxTransactionTypeCollection;
  }
}
