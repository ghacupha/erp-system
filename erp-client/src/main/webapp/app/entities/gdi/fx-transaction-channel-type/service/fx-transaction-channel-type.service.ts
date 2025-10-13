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
import { IFxTransactionChannelType, getFxTransactionChannelTypeIdentifier } from '../fx-transaction-channel-type.model';

export type EntityResponseType = HttpResponse<IFxTransactionChannelType>;
export type EntityArrayResponseType = HttpResponse<IFxTransactionChannelType[]>;

@Injectable({ providedIn: 'root' })
export class FxTransactionChannelTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-transaction-channel-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-transaction-channel-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxTransactionChannelType: IFxTransactionChannelType): Observable<EntityResponseType> {
    return this.http.post<IFxTransactionChannelType>(this.resourceUrl, fxTransactionChannelType, { observe: 'response' });
  }

  update(fxTransactionChannelType: IFxTransactionChannelType): Observable<EntityResponseType> {
    return this.http.put<IFxTransactionChannelType>(
      `${this.resourceUrl}/${getFxTransactionChannelTypeIdentifier(fxTransactionChannelType) as number}`,
      fxTransactionChannelType,
      { observe: 'response' }
    );
  }

  partialUpdate(fxTransactionChannelType: IFxTransactionChannelType): Observable<EntityResponseType> {
    return this.http.patch<IFxTransactionChannelType>(
      `${this.resourceUrl}/${getFxTransactionChannelTypeIdentifier(fxTransactionChannelType) as number}`,
      fxTransactionChannelType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxTransactionChannelType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionChannelType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionChannelType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxTransactionChannelTypeToCollectionIfMissing(
    fxTransactionChannelTypeCollection: IFxTransactionChannelType[],
    ...fxTransactionChannelTypesToCheck: (IFxTransactionChannelType | null | undefined)[]
  ): IFxTransactionChannelType[] {
    const fxTransactionChannelTypes: IFxTransactionChannelType[] = fxTransactionChannelTypesToCheck.filter(isPresent);
    if (fxTransactionChannelTypes.length > 0) {
      const fxTransactionChannelTypeCollectionIdentifiers = fxTransactionChannelTypeCollection.map(
        fxTransactionChannelTypeItem => getFxTransactionChannelTypeIdentifier(fxTransactionChannelTypeItem)!
      );
      const fxTransactionChannelTypesToAdd = fxTransactionChannelTypes.filter(fxTransactionChannelTypeItem => {
        const fxTransactionChannelTypeIdentifier = getFxTransactionChannelTypeIdentifier(fxTransactionChannelTypeItem);
        if (
          fxTransactionChannelTypeIdentifier == null ||
          fxTransactionChannelTypeCollectionIdentifiers.includes(fxTransactionChannelTypeIdentifier)
        ) {
          return false;
        }
        fxTransactionChannelTypeCollectionIdentifiers.push(fxTransactionChannelTypeIdentifier);
        return true;
      });
      return [...fxTransactionChannelTypesToAdd, ...fxTransactionChannelTypeCollection];
    }
    return fxTransactionChannelTypeCollection;
  }
}
