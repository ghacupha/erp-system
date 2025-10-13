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
import { IAssetDisposal, getAssetDisposalIdentifier } from '../asset-disposal.model';

export type EntityResponseType = HttpResponse<IAssetDisposal>;
export type EntityArrayResponseType = HttpResponse<IAssetDisposal[]>;

@Injectable({ providedIn: 'root' })
export class AssetDisposalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/asset-disposals');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/asset-disposals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetDisposal: IAssetDisposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDisposal);
    return this.http
      .post<IAssetDisposal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetDisposal: IAssetDisposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDisposal);
    return this.http
      .put<IAssetDisposal>(`${this.resourceUrl}/${getAssetDisposalIdentifier(assetDisposal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetDisposal: IAssetDisposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetDisposal);
    return this.http
      .patch<IAssetDisposal>(`${this.resourceUrl}/${getAssetDisposalIdentifier(assetDisposal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetDisposal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetDisposal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetDisposal[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetDisposalToCollectionIfMissing(
    assetDisposalCollection: IAssetDisposal[],
    ...assetDisposalsToCheck: (IAssetDisposal | null | undefined)[]
  ): IAssetDisposal[] {
    const assetDisposals: IAssetDisposal[] = assetDisposalsToCheck.filter(isPresent);
    if (assetDisposals.length > 0) {
      const assetDisposalCollectionIdentifiers = assetDisposalCollection.map(
        assetDisposalItem => getAssetDisposalIdentifier(assetDisposalItem)!
      );
      const assetDisposalsToAdd = assetDisposals.filter(assetDisposalItem => {
        const assetDisposalIdentifier = getAssetDisposalIdentifier(assetDisposalItem);
        if (assetDisposalIdentifier == null || assetDisposalCollectionIdentifiers.includes(assetDisposalIdentifier)) {
          return false;
        }
        assetDisposalCollectionIdentifiers.push(assetDisposalIdentifier);
        return true;
      });
      return [...assetDisposalsToAdd, ...assetDisposalCollection];
    }
    return assetDisposalCollection;
  }

  protected convertDateFromClient(assetDisposal: IAssetDisposal): IAssetDisposal {
    return Object.assign({}, assetDisposal, {
      decommissioningDate: assetDisposal.decommissioningDate?.isValid() ? assetDisposal.decommissioningDate.format(DATE_FORMAT) : undefined,
      disposalDate: assetDisposal.disposalDate?.isValid() ? assetDisposal.disposalDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.decommissioningDate = res.body.decommissioningDate ? dayjs(res.body.decommissioningDate) : undefined;
      res.body.disposalDate = res.body.disposalDate ? dayjs(res.body.disposalDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetDisposal: IAssetDisposal) => {
        assetDisposal.decommissioningDate = assetDisposal.decommissioningDate ? dayjs(assetDisposal.decommissioningDate) : undefined;
        assetDisposal.disposalDate = assetDisposal.disposalDate ? dayjs(assetDisposal.disposalDate) : undefined;
      });
    }
    return res;
  }
}
