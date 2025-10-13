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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAssetWarranty, getAssetWarrantyIdentifier } from '../asset-warranty.model';

export type EntityResponseType = HttpResponse<IAssetWarranty>;
export type EntityArrayResponseType = HttpResponse<IAssetWarranty[]>;

@Injectable({ providedIn: 'root' })
export class AssetWarrantyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/asset-warranties');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/app/_search/asset-warranties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetWarranty: IAssetWarranty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWarranty);
    return this.http
      .post<IAssetWarranty>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetWarranty: IAssetWarranty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWarranty);
    return this.http
      .put<IAssetWarranty>(`${this.resourceUrl}/${getAssetWarrantyIdentifier(assetWarranty) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetWarranty: IAssetWarranty): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWarranty);
    return this.http
      .patch<IAssetWarranty>(`${this.resourceUrl}/${getAssetWarrantyIdentifier(assetWarranty) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetWarranty>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetWarranty[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetWarranty[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetWarrantyToCollectionIfMissing(
    assetWarrantyCollection: IAssetWarranty[],
    ...assetWarrantiesToCheck: (IAssetWarranty | null | undefined)[]
  ): IAssetWarranty[] {
    const assetWarranties: IAssetWarranty[] = assetWarrantiesToCheck.filter(isPresent);
    if (assetWarranties.length > 0) {
      const assetWarrantyCollectionIdentifiers = assetWarrantyCollection.map(
        assetWarrantyItem => getAssetWarrantyIdentifier(assetWarrantyItem)!
      );
      const assetWarrantiesToAdd = assetWarranties.filter(assetWarrantyItem => {
        const assetWarrantyIdentifier = getAssetWarrantyIdentifier(assetWarrantyItem);
        if (assetWarrantyIdentifier == null || assetWarrantyCollectionIdentifiers.includes(assetWarrantyIdentifier)) {
          return false;
        }
        assetWarrantyCollectionIdentifiers.push(assetWarrantyIdentifier);
        return true;
      });
      return [...assetWarrantiesToAdd, ...assetWarrantyCollection];
    }
    return assetWarrantyCollection;
  }

  protected convertDateFromClient(assetWarranty: IAssetWarranty): IAssetWarranty {
    return Object.assign({}, assetWarranty, {
      expiryDate: assetWarranty.expiryDate?.isValid() ? assetWarranty.expiryDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expiryDate = res.body.expiryDate ? dayjs(res.body.expiryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetWarranty: IAssetWarranty) => {
        assetWarranty.expiryDate = assetWarranty.expiryDate ? dayjs(assetWarranty.expiryDate) : undefined;
      });
    }
    return res;
  }
}
