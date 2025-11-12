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
import { ICollateralType, getCollateralTypeIdentifier } from '../collateral-type.model';

export type EntityResponseType = HttpResponse<ICollateralType>;
export type EntityArrayResponseType = HttpResponse<ICollateralType[]>;

@Injectable({ providedIn: 'root' })
export class CollateralTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/collateral-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/collateral-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(collateralType: ICollateralType): Observable<EntityResponseType> {
    return this.http.post<ICollateralType>(this.resourceUrl, collateralType, { observe: 'response' });
  }

  update(collateralType: ICollateralType): Observable<EntityResponseType> {
    return this.http.put<ICollateralType>(`${this.resourceUrl}/${getCollateralTypeIdentifier(collateralType) as number}`, collateralType, {
      observe: 'response',
    });
  }

  partialUpdate(collateralType: ICollateralType): Observable<EntityResponseType> {
    return this.http.patch<ICollateralType>(
      `${this.resourceUrl}/${getCollateralTypeIdentifier(collateralType) as number}`,
      collateralType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICollateralType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollateralType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICollateralType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCollateralTypeToCollectionIfMissing(
    collateralTypeCollection: ICollateralType[],
    ...collateralTypesToCheck: (ICollateralType | null | undefined)[]
  ): ICollateralType[] {
    const collateralTypes: ICollateralType[] = collateralTypesToCheck.filter(isPresent);
    if (collateralTypes.length > 0) {
      const collateralTypeCollectionIdentifiers = collateralTypeCollection.map(
        collateralTypeItem => getCollateralTypeIdentifier(collateralTypeItem)!
      );
      const collateralTypesToAdd = collateralTypes.filter(collateralTypeItem => {
        const collateralTypeIdentifier = getCollateralTypeIdentifier(collateralTypeItem);
        if (collateralTypeIdentifier == null || collateralTypeCollectionIdentifiers.includes(collateralTypeIdentifier)) {
          return false;
        }
        collateralTypeCollectionIdentifiers.push(collateralTypeIdentifier);
        return true;
      });
      return [...collateralTypesToAdd, ...collateralTypeCollection];
    }
    return collateralTypeCollection;
  }
}
