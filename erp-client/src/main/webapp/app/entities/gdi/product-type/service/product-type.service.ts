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
import { IProductType, getProductTypeIdentifier } from '../product-type.model';

export type EntityResponseType = HttpResponse<IProductType>;
export type EntityArrayResponseType = HttpResponse<IProductType[]>;

@Injectable({ providedIn: 'root' })
export class ProductTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/product-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productType: IProductType): Observable<EntityResponseType> {
    return this.http.post<IProductType>(this.resourceUrl, productType, { observe: 'response' });
  }

  update(productType: IProductType): Observable<EntityResponseType> {
    return this.http.put<IProductType>(`${this.resourceUrl}/${getProductTypeIdentifier(productType) as number}`, productType, {
      observe: 'response',
    });
  }

  partialUpdate(productType: IProductType): Observable<EntityResponseType> {
    return this.http.patch<IProductType>(`${this.resourceUrl}/${getProductTypeIdentifier(productType) as number}`, productType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addProductTypeToCollectionIfMissing(
    productTypeCollection: IProductType[],
    ...productTypesToCheck: (IProductType | null | undefined)[]
  ): IProductType[] {
    const productTypes: IProductType[] = productTypesToCheck.filter(isPresent);
    if (productTypes.length > 0) {
      const productTypeCollectionIdentifiers = productTypeCollection.map(productTypeItem => getProductTypeIdentifier(productTypeItem)!);
      const productTypesToAdd = productTypes.filter(productTypeItem => {
        const productTypeIdentifier = getProductTypeIdentifier(productTypeItem);
        if (productTypeIdentifier == null || productTypeCollectionIdentifiers.includes(productTypeIdentifier)) {
          return false;
        }
        productTypeCollectionIdentifiers.push(productTypeIdentifier);
        return true;
      });
      return [...productTypesToAdd, ...productTypeCollection];
    }
    return productTypeCollection;
  }
}
