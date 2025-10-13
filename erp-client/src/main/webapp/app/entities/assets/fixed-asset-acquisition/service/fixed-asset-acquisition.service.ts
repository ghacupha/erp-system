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
import { IFixedAssetAcquisition, getFixedAssetAcquisitionIdentifier } from '../fixed-asset-acquisition.model';

export type EntityResponseType = HttpResponse<IFixedAssetAcquisition>;
export type EntityArrayResponseType = HttpResponse<IFixedAssetAcquisition[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetAcquisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset-acquisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fixed-asset-acquisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fixedAssetAcquisition: IFixedAssetAcquisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetAcquisition);
    return this.http
      .post<IFixedAssetAcquisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetAcquisition: IFixedAssetAcquisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetAcquisition);
    return this.http
      .put<IFixedAssetAcquisition>(`${this.resourceUrl}/${getFixedAssetAcquisitionIdentifier(fixedAssetAcquisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fixedAssetAcquisition: IFixedAssetAcquisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetAcquisition);
    return this.http
      .patch<IFixedAssetAcquisition>(`${this.resourceUrl}/${getFixedAssetAcquisitionIdentifier(fixedAssetAcquisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFixedAssetAcquisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetAcquisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetAcquisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFixedAssetAcquisitionToCollectionIfMissing(
    fixedAssetAcquisitionCollection: IFixedAssetAcquisition[],
    ...fixedAssetAcquisitionsToCheck: (IFixedAssetAcquisition | null | undefined)[]
  ): IFixedAssetAcquisition[] {
    const fixedAssetAcquisitions: IFixedAssetAcquisition[] = fixedAssetAcquisitionsToCheck.filter(isPresent);
    if (fixedAssetAcquisitions.length > 0) {
      const fixedAssetAcquisitionCollectionIdentifiers = fixedAssetAcquisitionCollection.map(
        fixedAssetAcquisitionItem => getFixedAssetAcquisitionIdentifier(fixedAssetAcquisitionItem)!
      );
      const fixedAssetAcquisitionsToAdd = fixedAssetAcquisitions.filter(fixedAssetAcquisitionItem => {
        const fixedAssetAcquisitionIdentifier = getFixedAssetAcquisitionIdentifier(fixedAssetAcquisitionItem);
        if (
          fixedAssetAcquisitionIdentifier == null ||
          fixedAssetAcquisitionCollectionIdentifiers.includes(fixedAssetAcquisitionIdentifier)
        ) {
          return false;
        }
        fixedAssetAcquisitionCollectionIdentifiers.push(fixedAssetAcquisitionIdentifier);
        return true;
      });
      return [...fixedAssetAcquisitionsToAdd, ...fixedAssetAcquisitionCollection];
    }
    return fixedAssetAcquisitionCollection;
  }

  protected convertDateFromClient(fixedAssetAcquisition: IFixedAssetAcquisition): IFixedAssetAcquisition {
    return Object.assign({}, fixedAssetAcquisition, {
      purchaseDate: fixedAssetAcquisition.purchaseDate?.isValid() ? fixedAssetAcquisition.purchaseDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.purchaseDate = res.body.purchaseDate ? dayjs(res.body.purchaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetAcquisition: IFixedAssetAcquisition) => {
        fixedAssetAcquisition.purchaseDate = fixedAssetAcquisition.purchaseDate ? dayjs(fixedAssetAcquisition.purchaseDate) : undefined;
      });
    }
    return res;
  }
}
