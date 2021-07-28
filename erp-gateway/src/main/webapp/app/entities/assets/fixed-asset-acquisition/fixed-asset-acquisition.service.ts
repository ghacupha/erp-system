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
import { IFixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';

type EntityResponseType = HttpResponse<IFixedAssetAcquisition>;
type EntityArrayResponseType = HttpResponse<IFixedAssetAcquisition[]>;

@Injectable({ providedIn: 'root' })
export class FixedAssetAcquisitionService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/fixed-asset-acquisitions';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/fixed-asset-acquisitions';

  constructor(protected http: HttpClient) {}

  create(fixedAssetAcquisition: IFixedAssetAcquisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetAcquisition);
    return this.http
      .post<IFixedAssetAcquisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fixedAssetAcquisition: IFixedAssetAcquisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fixedAssetAcquisition);
    return this.http
      .put<IFixedAssetAcquisition>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(fixedAssetAcquisition: IFixedAssetAcquisition): IFixedAssetAcquisition {
    const copy: IFixedAssetAcquisition = Object.assign({}, fixedAssetAcquisition, {
      purchaseDate:
        fixedAssetAcquisition.purchaseDate && fixedAssetAcquisition.purchaseDate.isValid()
          ? fixedAssetAcquisition.purchaseDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.purchaseDate = res.body.purchaseDate ? moment(res.body.purchaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fixedAssetAcquisition: IFixedAssetAcquisition) => {
        fixedAssetAcquisition.purchaseDate = fixedAssetAcquisition.purchaseDate ? moment(fixedAssetAcquisition.purchaseDate) : undefined;
      });
    }
    return res;
  }
}
