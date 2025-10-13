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
import { IFxCustomerType, getFxCustomerTypeIdentifier } from '../fx-customer-type.model';

export type EntityResponseType = HttpResponse<IFxCustomerType>;
export type EntityArrayResponseType = HttpResponse<IFxCustomerType[]>;

@Injectable({ providedIn: 'root' })
export class FxCustomerTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-customer-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-customer-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxCustomerType: IFxCustomerType): Observable<EntityResponseType> {
    return this.http.post<IFxCustomerType>(this.resourceUrl, fxCustomerType, { observe: 'response' });
  }

  update(fxCustomerType: IFxCustomerType): Observable<EntityResponseType> {
    return this.http.put<IFxCustomerType>(`${this.resourceUrl}/${getFxCustomerTypeIdentifier(fxCustomerType) as number}`, fxCustomerType, {
      observe: 'response',
    });
  }

  partialUpdate(fxCustomerType: IFxCustomerType): Observable<EntityResponseType> {
    return this.http.patch<IFxCustomerType>(
      `${this.resourceUrl}/${getFxCustomerTypeIdentifier(fxCustomerType) as number}`,
      fxCustomerType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxCustomerType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxCustomerType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxCustomerType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxCustomerTypeToCollectionIfMissing(
    fxCustomerTypeCollection: IFxCustomerType[],
    ...fxCustomerTypesToCheck: (IFxCustomerType | null | undefined)[]
  ): IFxCustomerType[] {
    const fxCustomerTypes: IFxCustomerType[] = fxCustomerTypesToCheck.filter(isPresent);
    if (fxCustomerTypes.length > 0) {
      const fxCustomerTypeCollectionIdentifiers = fxCustomerTypeCollection.map(
        fxCustomerTypeItem => getFxCustomerTypeIdentifier(fxCustomerTypeItem)!
      );
      const fxCustomerTypesToAdd = fxCustomerTypes.filter(fxCustomerTypeItem => {
        const fxCustomerTypeIdentifier = getFxCustomerTypeIdentifier(fxCustomerTypeItem);
        if (fxCustomerTypeIdentifier == null || fxCustomerTypeCollectionIdentifiers.includes(fxCustomerTypeIdentifier)) {
          return false;
        }
        fxCustomerTypeCollectionIdentifiers.push(fxCustomerTypeIdentifier);
        return true;
      });
      return [...fxCustomerTypesToAdd, ...fxCustomerTypeCollection];
    }
    return fxCustomerTypeCollection;
  }
}
