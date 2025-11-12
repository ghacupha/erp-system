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
import { ICounterpartyType, getCounterpartyTypeIdentifier } from '../counterparty-type.model';

export type EntityResponseType = HttpResponse<ICounterpartyType>;
export type EntityArrayResponseType = HttpResponse<ICounterpartyType[]>;

@Injectable({ providedIn: 'root' })
export class CounterpartyTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counterparty-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/counterparty-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterpartyType: ICounterpartyType): Observable<EntityResponseType> {
    return this.http.post<ICounterpartyType>(this.resourceUrl, counterpartyType, { observe: 'response' });
  }

  update(counterpartyType: ICounterpartyType): Observable<EntityResponseType> {
    return this.http.put<ICounterpartyType>(
      `${this.resourceUrl}/${getCounterpartyTypeIdentifier(counterpartyType) as number}`,
      counterpartyType,
      { observe: 'response' }
    );
  }

  partialUpdate(counterpartyType: ICounterpartyType): Observable<EntityResponseType> {
    return this.http.patch<ICounterpartyType>(
      `${this.resourceUrl}/${getCounterpartyTypeIdentifier(counterpartyType) as number}`,
      counterpartyType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICounterpartyType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterpartyType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterpartyType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCounterpartyTypeToCollectionIfMissing(
    counterpartyTypeCollection: ICounterpartyType[],
    ...counterpartyTypesToCheck: (ICounterpartyType | null | undefined)[]
  ): ICounterpartyType[] {
    const counterpartyTypes: ICounterpartyType[] = counterpartyTypesToCheck.filter(isPresent);
    if (counterpartyTypes.length > 0) {
      const counterpartyTypeCollectionIdentifiers = counterpartyTypeCollection.map(
        counterpartyTypeItem => getCounterpartyTypeIdentifier(counterpartyTypeItem)!
      );
      const counterpartyTypesToAdd = counterpartyTypes.filter(counterpartyTypeItem => {
        const counterpartyTypeIdentifier = getCounterpartyTypeIdentifier(counterpartyTypeItem);
        if (counterpartyTypeIdentifier == null || counterpartyTypeCollectionIdentifiers.includes(counterpartyTypeIdentifier)) {
          return false;
        }
        counterpartyTypeCollectionIdentifiers.push(counterpartyTypeIdentifier);
        return true;
      });
      return [...counterpartyTypesToAdd, ...counterpartyTypeCollection];
    }
    return counterpartyTypeCollection;
  }
}
