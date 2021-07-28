///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IFixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';

type EntityResponseType = HttpResponse<IFixedAssetNetBookValue>;
type EntityArrayResponseType = HttpResponse<IFixedAssetNetBookValue[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetNetBookValueService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/fixed-asset-net-book-values';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/fixed-asset-net-book-values';

  constructor(protected http: HttpClient) {}

  create(fixedAssetNetBookValue: IFixedAssetNetBookValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetNetBookValue);
    return this.http
      .post<IFixedAssetNetBookValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetNetBookValue: IFixedAssetNetBookValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetNetBookValue);
    return this.http
      .put<IFixedAssetNetBookValue>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(fixedAssetNetBookValue: IFixedAssetNetBookValue): IFixedAssetNetBookValue {
    const copy: IFixedAssetNetBookValue = Object.assign({}, fixedAssetNetBookValue, {
      netBookValueDate:
        fixedAssetNetBookValue.netBookValueDate && fixedAssetNetBookValue.netBookValueDate.isValid()
          ? fixedAssetNetBookValue.netBookValueDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.netBookValueDate = res.body.netBookValueDate ? moment(res.body.netBookValueDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetNetBookValue: IFixedAssetNetBookValue) => {
        fixedAssetNetBookValue.netBookValueDate = fixedAssetNetBookValue.netBookValueDate
          ? moment(fixedAssetNetBookValue.netBookValueDate)
          : undefined;
      });
    }
    return res;
  }
}
