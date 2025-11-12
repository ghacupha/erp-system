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
import { IDerivativeUnderlyingAsset, getDerivativeUnderlyingAssetIdentifier } from '../derivative-underlying-asset.model';

export type EntityResponseType = HttpResponse<IDerivativeUnderlyingAsset>;
export type EntityArrayResponseType = HttpResponse<IDerivativeUnderlyingAsset[]>;

@Injectable({ providedIn: 'root' })
export class DerivativeUnderlyingAssetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/derivative-underlying-assets');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/derivative-underlying-assets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(derivativeUnderlyingAsset: IDerivativeUnderlyingAsset): Observable<EntityResponseType> {
    return this.http.post<IDerivativeUnderlyingAsset>(this.resourceUrl, derivativeUnderlyingAsset, { observe: 'response' });
  }

  update(derivativeUnderlyingAsset: IDerivativeUnderlyingAsset): Observable<EntityResponseType> {
    return this.http.put<IDerivativeUnderlyingAsset>(
      `${this.resourceUrl}/${getDerivativeUnderlyingAssetIdentifier(derivativeUnderlyingAsset) as number}`,
      derivativeUnderlyingAsset,
      { observe: 'response' }
    );
  }

  partialUpdate(derivativeUnderlyingAsset: IDerivativeUnderlyingAsset): Observable<EntityResponseType> {
    return this.http.patch<IDerivativeUnderlyingAsset>(
      `${this.resourceUrl}/${getDerivativeUnderlyingAssetIdentifier(derivativeUnderlyingAsset) as number}`,
      derivativeUnderlyingAsset,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDerivativeUnderlyingAsset>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDerivativeUnderlyingAsset[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDerivativeUnderlyingAsset[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDerivativeUnderlyingAssetToCollectionIfMissing(
    derivativeUnderlyingAssetCollection: IDerivativeUnderlyingAsset[],
    ...derivativeUnderlyingAssetsToCheck: (IDerivativeUnderlyingAsset | null | undefined)[]
  ): IDerivativeUnderlyingAsset[] {
    const derivativeUnderlyingAssets: IDerivativeUnderlyingAsset[] = derivativeUnderlyingAssetsToCheck.filter(isPresent);
    if (derivativeUnderlyingAssets.length > 0) {
      const derivativeUnderlyingAssetCollectionIdentifiers = derivativeUnderlyingAssetCollection.map(
        derivativeUnderlyingAssetItem => getDerivativeUnderlyingAssetIdentifier(derivativeUnderlyingAssetItem)!
      );
      const derivativeUnderlyingAssetsToAdd = derivativeUnderlyingAssets.filter(derivativeUnderlyingAssetItem => {
        const derivativeUnderlyingAssetIdentifier = getDerivativeUnderlyingAssetIdentifier(derivativeUnderlyingAssetItem);
        if (
          derivativeUnderlyingAssetIdentifier == null ||
          derivativeUnderlyingAssetCollectionIdentifiers.includes(derivativeUnderlyingAssetIdentifier)
        ) {
          return false;
        }
        derivativeUnderlyingAssetCollectionIdentifiers.push(derivativeUnderlyingAssetIdentifier);
        return true;
      });
      return [...derivativeUnderlyingAssetsToAdd, ...derivativeUnderlyingAssetCollection];
    }
    return derivativeUnderlyingAssetCollection;
  }
}
