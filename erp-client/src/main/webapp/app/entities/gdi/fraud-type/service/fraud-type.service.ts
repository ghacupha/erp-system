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
import { IFraudType, getFraudTypeIdentifier } from '../fraud-type.model';

export type EntityResponseType = HttpResponse<IFraudType>;
export type EntityArrayResponseType = HttpResponse<IFraudType[]>;

@Injectable({ providedIn: 'root' })
export class FraudTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fraud-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fraud-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fraudType: IFraudType): Observable<EntityResponseType> {
    return this.http.post<IFraudType>(this.resourceUrl, fraudType, { observe: 'response' });
  }

  update(fraudType: IFraudType): Observable<EntityResponseType> {
    return this.http.put<IFraudType>(`${this.resourceUrl}/${getFraudTypeIdentifier(fraudType) as number}`, fraudType, {
      observe: 'response',
    });
  }

  partialUpdate(fraudType: IFraudType): Observable<EntityResponseType> {
    return this.http.patch<IFraudType>(`${this.resourceUrl}/${getFraudTypeIdentifier(fraudType) as number}`, fraudType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFraudType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFraudType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFraudType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFraudTypeToCollectionIfMissing(
    fraudTypeCollection: IFraudType[],
    ...fraudTypesToCheck: (IFraudType | null | undefined)[]
  ): IFraudType[] {
    const fraudTypes: IFraudType[] = fraudTypesToCheck.filter(isPresent);
    if (fraudTypes.length > 0) {
      const fraudTypeCollectionIdentifiers = fraudTypeCollection.map(fraudTypeItem => getFraudTypeIdentifier(fraudTypeItem)!);
      const fraudTypesToAdd = fraudTypes.filter(fraudTypeItem => {
        const fraudTypeIdentifier = getFraudTypeIdentifier(fraudTypeItem);
        if (fraudTypeIdentifier == null || fraudTypeCollectionIdentifiers.includes(fraudTypeIdentifier)) {
          return false;
        }
        fraudTypeCollectionIdentifiers.push(fraudTypeIdentifier);
        return true;
      });
      return [...fraudTypesToAdd, ...fraudTypeCollection];
    }
    return fraudTypeCollection;
  }
}
