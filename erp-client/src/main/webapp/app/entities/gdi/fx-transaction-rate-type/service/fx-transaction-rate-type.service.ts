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
import { IFxTransactionRateType, getFxTransactionRateTypeIdentifier } from '../fx-transaction-rate-type.model';

export type EntityResponseType = HttpResponse<IFxTransactionRateType>;
export type EntityArrayResponseType = HttpResponse<IFxTransactionRateType[]>;

@Injectable({ providedIn: 'root' })
export class FxTransactionRateTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-transaction-rate-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-transaction-rate-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxTransactionRateType: IFxTransactionRateType): Observable<EntityResponseType> {
    return this.http.post<IFxTransactionRateType>(this.resourceUrl, fxTransactionRateType, { observe: 'response' });
  }

  update(fxTransactionRateType: IFxTransactionRateType): Observable<EntityResponseType> {
    return this.http.put<IFxTransactionRateType>(
      `${this.resourceUrl}/${getFxTransactionRateTypeIdentifier(fxTransactionRateType) as number}`,
      fxTransactionRateType,
      { observe: 'response' }
    );
  }

  partialUpdate(fxTransactionRateType: IFxTransactionRateType): Observable<EntityResponseType> {
    return this.http.patch<IFxTransactionRateType>(
      `${this.resourceUrl}/${getFxTransactionRateTypeIdentifier(fxTransactionRateType) as number}`,
      fxTransactionRateType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxTransactionRateType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionRateType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxTransactionRateType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxTransactionRateTypeToCollectionIfMissing(
    fxTransactionRateTypeCollection: IFxTransactionRateType[],
    ...fxTransactionRateTypesToCheck: (IFxTransactionRateType | null | undefined)[]
  ): IFxTransactionRateType[] {
    const fxTransactionRateTypes: IFxTransactionRateType[] = fxTransactionRateTypesToCheck.filter(isPresent);
    if (fxTransactionRateTypes.length > 0) {
      const fxTransactionRateTypeCollectionIdentifiers = fxTransactionRateTypeCollection.map(
        fxTransactionRateTypeItem => getFxTransactionRateTypeIdentifier(fxTransactionRateTypeItem)!
      );
      const fxTransactionRateTypesToAdd = fxTransactionRateTypes.filter(fxTransactionRateTypeItem => {
        const fxTransactionRateTypeIdentifier = getFxTransactionRateTypeIdentifier(fxTransactionRateTypeItem);
        if (
          fxTransactionRateTypeIdentifier == null ||
          fxTransactionRateTypeCollectionIdentifiers.includes(fxTransactionRateTypeIdentifier)
        ) {
          return false;
        }
        fxTransactionRateTypeCollectionIdentifiers.push(fxTransactionRateTypeIdentifier);
        return true;
      });
      return [...fxTransactionRateTypesToAdd, ...fxTransactionRateTypeCollection];
    }
    return fxTransactionRateTypeCollection;
  }
}
