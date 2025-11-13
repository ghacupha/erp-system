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
import {
  IAgriculturalEnterpriseActivityType,
  getAgriculturalEnterpriseActivityTypeIdentifier,
} from '../agricultural-enterprise-activity-type.model';

export type EntityResponseType = HttpResponse<IAgriculturalEnterpriseActivityType>;
export type EntityArrayResponseType = HttpResponse<IAgriculturalEnterpriseActivityType[]>;

@Injectable({ providedIn: 'root' })
export class AgriculturalEnterpriseActivityTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agricultural-enterprise-activity-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/agricultural-enterprise-activity-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType): Observable<EntityResponseType> {
    return this.http.post<IAgriculturalEnterpriseActivityType>(this.resourceUrl, agriculturalEnterpriseActivityType, {
      observe: 'response',
    });
  }

  update(agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType): Observable<EntityResponseType> {
    return this.http.put<IAgriculturalEnterpriseActivityType>(
      `${this.resourceUrl}/${getAgriculturalEnterpriseActivityTypeIdentifier(agriculturalEnterpriseActivityType) as number}`,
      agriculturalEnterpriseActivityType,
      { observe: 'response' }
    );
  }

  partialUpdate(agriculturalEnterpriseActivityType: IAgriculturalEnterpriseActivityType): Observable<EntityResponseType> {
    return this.http.patch<IAgriculturalEnterpriseActivityType>(
      `${this.resourceUrl}/${getAgriculturalEnterpriseActivityTypeIdentifier(agriculturalEnterpriseActivityType) as number}`,
      agriculturalEnterpriseActivityType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgriculturalEnterpriseActivityType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgriculturalEnterpriseActivityType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgriculturalEnterpriseActivityType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAgriculturalEnterpriseActivityTypeToCollectionIfMissing(
    agriculturalEnterpriseActivityTypeCollection: IAgriculturalEnterpriseActivityType[],
    ...agriculturalEnterpriseActivityTypesToCheck: (IAgriculturalEnterpriseActivityType | null | undefined)[]
  ): IAgriculturalEnterpriseActivityType[] {
    const agriculturalEnterpriseActivityTypes: IAgriculturalEnterpriseActivityType[] =
      agriculturalEnterpriseActivityTypesToCheck.filter(isPresent);
    if (agriculturalEnterpriseActivityTypes.length > 0) {
      const agriculturalEnterpriseActivityTypeCollectionIdentifiers = agriculturalEnterpriseActivityTypeCollection.map(
        agriculturalEnterpriseActivityTypeItem => getAgriculturalEnterpriseActivityTypeIdentifier(agriculturalEnterpriseActivityTypeItem)!
      );
      const agriculturalEnterpriseActivityTypesToAdd = agriculturalEnterpriseActivityTypes.filter(
        agriculturalEnterpriseActivityTypeItem => {
          const agriculturalEnterpriseActivityTypeIdentifier = getAgriculturalEnterpriseActivityTypeIdentifier(
            agriculturalEnterpriseActivityTypeItem
          );
          if (
            agriculturalEnterpriseActivityTypeIdentifier == null ||
            agriculturalEnterpriseActivityTypeCollectionIdentifiers.includes(agriculturalEnterpriseActivityTypeIdentifier)
          ) {
            return false;
          }
          agriculturalEnterpriseActivityTypeCollectionIdentifiers.push(agriculturalEnterpriseActivityTypeIdentifier);
          return true;
        }
      );
      return [...agriculturalEnterpriseActivityTypesToAdd, ...agriculturalEnterpriseActivityTypeCollection];
    }
    return agriculturalEnterpriseActivityTypeCollection;
  }
}
