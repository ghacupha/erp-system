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
import { IFixedAssetDepreciation, getFixedAssetDepreciationIdentifier } from '../fixed-asset-depreciation.model';

export type EntityResponseType = HttpResponse<IFixedAssetDepreciation>;
export type EntityArrayResponseType = HttpResponse<IFixedAssetDepreciation[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetDepreciationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/fixed-asset-depreciations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/fixed-asset-depreciations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fixedAssetDepreciation: IFixedAssetDepreciation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetDepreciation);
    return this.http
      .post<IFixedAssetDepreciation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetDepreciation: IFixedAssetDepreciation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetDepreciation);
    return this.http
      .put<IFixedAssetDepreciation>(`${this.resourceUrl}/${getFixedAssetDepreciationIdentifier(fixedAssetDepreciation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fixedAssetDepreciation: IFixedAssetDepreciation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetDepreciation);
    return this.http
      .patch<IFixedAssetDepreciation>(
        `${this.resourceUrl}/${getFixedAssetDepreciationIdentifier(fixedAssetDepreciation) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFixedAssetDepreciation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetDepreciation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetDepreciation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFixedAssetDepreciationToCollectionIfMissing(
    fixedAssetDepreciationCollection: IFixedAssetDepreciation[],
    ...fixedAssetDepreciationsToCheck: (IFixedAssetDepreciation | null | undefined)[]
  ): IFixedAssetDepreciation[] {
    const fixedAssetDepreciations: IFixedAssetDepreciation[] = fixedAssetDepreciationsToCheck.filter(isPresent);
    if (fixedAssetDepreciations.length > 0) {
      const fixedAssetDepreciationCollectionIdentifiers = fixedAssetDepreciationCollection.map(
        fixedAssetDepreciationItem => getFixedAssetDepreciationIdentifier(fixedAssetDepreciationItem)!
      );
      const fixedAssetDepreciationsToAdd = fixedAssetDepreciations.filter(fixedAssetDepreciationItem => {
        const fixedAssetDepreciationIdentifier = getFixedAssetDepreciationIdentifier(fixedAssetDepreciationItem);
        if (
          fixedAssetDepreciationIdentifier == null ||
          fixedAssetDepreciationCollectionIdentifiers.includes(fixedAssetDepreciationIdentifier)
        ) {
          return false;
        }
        fixedAssetDepreciationCollectionIdentifiers.push(fixedAssetDepreciationIdentifier);
        return true;
      });
      return [...fixedAssetDepreciationsToAdd, ...fixedAssetDepreciationCollection];
    }
    return fixedAssetDepreciationCollection;
  }

  protected convertDateFromClient(fixedAssetDepreciation: IFixedAssetDepreciation): IFixedAssetDepreciation {
    return Object.assign({}, fixedAssetDepreciation, {
      depreciationDate: fixedAssetDepreciation.depreciationDate?.isValid()
        ? fixedAssetDepreciation.depreciationDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.depreciationDate = res.body.depreciationDate ? dayjs(res.body.depreciationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetDepreciation: IFixedAssetDepreciation) => {
        fixedAssetDepreciation.depreciationDate = fixedAssetDepreciation.depreciationDate
          ? dayjs(fixedAssetDepreciation.depreciationDate)
          : undefined;
      });
    }
    return res;
  }
}
