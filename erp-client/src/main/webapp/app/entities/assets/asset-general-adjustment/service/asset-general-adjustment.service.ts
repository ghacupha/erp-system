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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAssetGeneralAdjustment, getAssetGeneralAdjustmentIdentifier } from '../asset-general-adjustment.model';

export type EntityResponseType = HttpResponse<IAssetGeneralAdjustment>;
export type EntityArrayResponseType = HttpResponse<IAssetGeneralAdjustment[]>;

@Injectable({ providedIn: 'root' })
export class AssetGeneralAdjustmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-general-adjustments');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-general-adjustments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetGeneralAdjustment: IAssetGeneralAdjustment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetGeneralAdjustment);
    return this.http
      .post<IAssetGeneralAdjustment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetGeneralAdjustment: IAssetGeneralAdjustment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetGeneralAdjustment);
    return this.http
      .put<IAssetGeneralAdjustment>(`${this.resourceUrl}/${getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustment) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetGeneralAdjustment: IAssetGeneralAdjustment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetGeneralAdjustment);
    return this.http
      .patch<IAssetGeneralAdjustment>(
        `${this.resourceUrl}/${getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustment) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetGeneralAdjustment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetGeneralAdjustment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetGeneralAdjustment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetGeneralAdjustmentToCollectionIfMissing(
    assetGeneralAdjustmentCollection: IAssetGeneralAdjustment[],
    ...assetGeneralAdjustmentsToCheck: (IAssetGeneralAdjustment | null | undefined)[]
  ): IAssetGeneralAdjustment[] {
    const assetGeneralAdjustments: IAssetGeneralAdjustment[] = assetGeneralAdjustmentsToCheck.filter(isPresent);
    if (assetGeneralAdjustments.length > 0) {
      const assetGeneralAdjustmentCollectionIdentifiers = assetGeneralAdjustmentCollection.map(
        assetGeneralAdjustmentItem => getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustmentItem)!
      );
      const assetGeneralAdjustmentsToAdd = assetGeneralAdjustments.filter(assetGeneralAdjustmentItem => {
        const assetGeneralAdjustmentIdentifier = getAssetGeneralAdjustmentIdentifier(assetGeneralAdjustmentItem);
        if (
          assetGeneralAdjustmentIdentifier == null ||
          assetGeneralAdjustmentCollectionIdentifiers.includes(assetGeneralAdjustmentIdentifier)
        ) {
          return false;
        }
        assetGeneralAdjustmentCollectionIdentifiers.push(assetGeneralAdjustmentIdentifier);
        return true;
      });
      return [...assetGeneralAdjustmentsToAdd, ...assetGeneralAdjustmentCollection];
    }
    return assetGeneralAdjustmentCollection;
  }

  protected convertDateFromClient(assetGeneralAdjustment: IAssetGeneralAdjustment): IAssetGeneralAdjustment {
    return Object.assign({}, assetGeneralAdjustment, {
      adjustmentDate: assetGeneralAdjustment.adjustmentDate?.isValid()
        ? assetGeneralAdjustment.adjustmentDate.format(DATE_FORMAT)
        : undefined,
      timeOfCreation: assetGeneralAdjustment.timeOfCreation?.isValid() ? assetGeneralAdjustment.timeOfCreation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.adjustmentDate = res.body.adjustmentDate ? dayjs(res.body.adjustmentDate) : undefined;
      res.body.timeOfCreation = res.body.timeOfCreation ? dayjs(res.body.timeOfCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetGeneralAdjustment: IAssetGeneralAdjustment) => {
        assetGeneralAdjustment.adjustmentDate = assetGeneralAdjustment.adjustmentDate
          ? dayjs(assetGeneralAdjustment.adjustmentDate)
          : undefined;
        assetGeneralAdjustment.timeOfCreation = assetGeneralAdjustment.timeOfCreation
          ? dayjs(assetGeneralAdjustment.timeOfCreation)
          : undefined;
      });
    }
    return res;
  }
}
