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
import { IAssetAccessory, getAssetAccessoryIdentifier } from '../asset-accessory.model';

export type EntityResponseType = HttpResponse<IAssetAccessory>;
export type EntityArrayResponseType = HttpResponse<IAssetAccessory[]>;

@Injectable({ providedIn: 'root' })
export class AssetAccessoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-accessories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-accessories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetAccessory: IAssetAccessory): Observable<EntityResponseType> {
    return this.http.post<IAssetAccessory>(this.resourceUrl, assetAccessory, { observe: 'response' });
  }

  update(assetAccessory: IAssetAccessory): Observable<EntityResponseType> {
    return this.http.put<IAssetAccessory>(`${this.resourceUrl}/${getAssetAccessoryIdentifier(assetAccessory) as number}`, assetAccessory, {
      observe: 'response',
    });
  }

  partialUpdate(assetAccessory: IAssetAccessory): Observable<EntityResponseType> {
    return this.http.patch<IAssetAccessory>(
      `${this.resourceUrl}/${getAssetAccessoryIdentifier(assetAccessory) as number}`,
      assetAccessory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssetAccessory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetAccessory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssetAccessory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAssetAccessoryToCollectionIfMissing(
    assetAccessoryCollection: IAssetAccessory[],
    ...assetAccessoriesToCheck: (IAssetAccessory | null | undefined)[]
  ): IAssetAccessory[] {
    const assetAccessories: IAssetAccessory[] = assetAccessoriesToCheck.filter(isPresent);
    if (assetAccessories.length > 0) {
      const assetAccessoryCollectionIdentifiers = assetAccessoryCollection.map(
        assetAccessoryItem => getAssetAccessoryIdentifier(assetAccessoryItem)!
      );
      const assetAccessoriesToAdd = assetAccessories.filter(assetAccessoryItem => {
        const assetAccessoryIdentifier = getAssetAccessoryIdentifier(assetAccessoryItem);
        if (assetAccessoryIdentifier == null || assetAccessoryCollectionIdentifiers.includes(assetAccessoryIdentifier)) {
          return false;
        }
        assetAccessoryCollectionIdentifiers.push(assetAccessoryIdentifier);
        return true;
      });
      return [...assetAccessoriesToAdd, ...assetAccessoryCollection];
    }
    return assetAccessoryCollection;
  }
}
