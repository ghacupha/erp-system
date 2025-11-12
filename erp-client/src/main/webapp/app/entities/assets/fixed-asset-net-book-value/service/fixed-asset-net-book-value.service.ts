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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFixedAssetNetBookValue, getFixedAssetNetBookValueIdentifier } from '../fixed-asset-net-book-value.model';

export type EntityResponseType = HttpResponse<IFixedAssetNetBookValue>;
export type EntityArrayResponseType = HttpResponse<IFixedAssetNetBookValue[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetNetBookValueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset-net-book-values');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fixed-asset-net-book-values');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fixedAssetNetBookValue: IFixedAssetNetBookValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetNetBookValue);
    return this.http
      .post<IFixedAssetNetBookValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetNetBookValue: IFixedAssetNetBookValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetNetBookValue);
    return this.http
      .put<IFixedAssetNetBookValue>(`${this.resourceUrl}/${getFixedAssetNetBookValueIdentifier(fixedAssetNetBookValue) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fixedAssetNetBookValue: IFixedAssetNetBookValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetNetBookValue);
    return this.http
      .patch<IFixedAssetNetBookValue>(
        `${this.resourceUrl}/${getFixedAssetNetBookValueIdentifier(fixedAssetNetBookValue) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFixedAssetNetBookValue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetNetBookValue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFixedAssetNetBookValue[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFixedAssetNetBookValueToCollectionIfMissing(
    fixedAssetNetBookValueCollection: IFixedAssetNetBookValue[],
    ...fixedAssetNetBookValuesToCheck: (IFixedAssetNetBookValue | null | undefined)[]
  ): IFixedAssetNetBookValue[] {
    const fixedAssetNetBookValues: IFixedAssetNetBookValue[] = fixedAssetNetBookValuesToCheck.filter(isPresent);
    if (fixedAssetNetBookValues.length > 0) {
      const fixedAssetNetBookValueCollectionIdentifiers = fixedAssetNetBookValueCollection.map(
        fixedAssetNetBookValueItem => getFixedAssetNetBookValueIdentifier(fixedAssetNetBookValueItem)!
      );
      const fixedAssetNetBookValuesToAdd = fixedAssetNetBookValues.filter(fixedAssetNetBookValueItem => {
        const fixedAssetNetBookValueIdentifier = getFixedAssetNetBookValueIdentifier(fixedAssetNetBookValueItem);
        if (
          fixedAssetNetBookValueIdentifier == null ||
          fixedAssetNetBookValueCollectionIdentifiers.includes(fixedAssetNetBookValueIdentifier)
        ) {
          return false;
        }
        fixedAssetNetBookValueCollectionIdentifiers.push(fixedAssetNetBookValueIdentifier);
        return true;
      });
      return [...fixedAssetNetBookValuesToAdd, ...fixedAssetNetBookValueCollection];
    }
    return fixedAssetNetBookValueCollection;
  }

  protected convertDateFromClient(fixedAssetNetBookValue: IFixedAssetNetBookValue): IFixedAssetNetBookValue {
    return Object.assign({}, fixedAssetNetBookValue, {
      netBookValueDate: fixedAssetNetBookValue.netBookValueDate?.isValid()
        ? fixedAssetNetBookValue.netBookValueDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.netBookValueDate = res.body.netBookValueDate ? dayjs(res.body.netBookValueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetNetBookValue: IFixedAssetNetBookValue) => {
        fixedAssetNetBookValue.netBookValueDate = fixedAssetNetBookValue.netBookValueDate
          ? dayjs(fixedAssetNetBookValue.netBookValueDate)
          : undefined;
      });
    }
    return res;
  }
}
