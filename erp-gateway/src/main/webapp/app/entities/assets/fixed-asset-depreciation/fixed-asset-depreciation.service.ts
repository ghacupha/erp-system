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
import { IFixedAssetDepreciation } from 'app/shared/model/assets/fixed-asset-depreciation.model';

type EntityResponseType = HttpResponse<IFixedAssetDepreciation>;
type EntityArrayResponseType = HttpResponse<IFixedAssetDepreciation[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetDepreciationService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/fixed-asset-depreciations';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/fixed-asset-depreciations';

  constructor(protected http: HttpClient) {}

  create(fixedAssetDepreciation: IFixedAssetDepreciation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetDepreciation);
    return this.http
      .post<IFixedAssetDepreciation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetDepreciation: IFixedAssetDepreciation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetDepreciation);
    return this.http
      .put<IFixedAssetDepreciation>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(fixedAssetDepreciation: IFixedAssetDepreciation): IFixedAssetDepreciation {
    const copy: IFixedAssetDepreciation = Object.assign({}, fixedAssetDepreciation, {
      depreciationDate:
        fixedAssetDepreciation.depreciationDate && fixedAssetDepreciation.depreciationDate.isValid()
          ? fixedAssetDepreciation.depreciationDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.depreciationDate = res.body.depreciationDate ? moment(res.body.depreciationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetDepreciation: IFixedAssetDepreciation) => {
        fixedAssetDepreciation.depreciationDate = fixedAssetDepreciation.depreciationDate
          ? moment(fixedAssetDepreciation.depreciationDate)
          : undefined;
      });
    }
    return res;
  }
}
