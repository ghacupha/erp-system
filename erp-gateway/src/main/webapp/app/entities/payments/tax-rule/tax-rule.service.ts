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
import { ITaxRule } from 'app/shared/model/payments/tax-rule.model';

type EntityResponseType = HttpResponse<ITaxRule>;
type EntityArrayResponseType = HttpResponse<ITaxRule[]>;

@Injectable({ providedIn: 'root' })
export class TaxRuleService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/tax-rules';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/tax-rules';

  constructor(protected http: HttpClient) {}

  create(taxRule: ITaxRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxRule);
    return this.http
      .post<ITaxRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taxRule: ITaxRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxRule);
    return this.http
      .put<ITaxRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaxRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(taxRule: ITaxRule): ITaxRule {
    const copy: ITaxRule = Object.assign({}, taxRule, {
      paymentDate: taxRule.paymentDate && taxRule.paymentDate.isValid() ? taxRule.paymentDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? moment(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((taxRule: ITaxRule) => {
        taxRule.paymentDate = taxRule.paymentDate ? moment(taxRule.paymentDate) : undefined;
      });
    }
    return res;
  }
}
