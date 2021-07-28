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

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPaymentRequisition } from 'app/shared/model/payments/payment-requisition.model';

type EntityResponseType = HttpResponse<IPaymentRequisition>;
type EntityArrayResponseType = HttpResponse<IPaymentRequisition[]>;

@Injectable({ providedIn: 'root' })
export class PaymentRequisitionService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/payment-requisitions';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/payment-requisitions';

  constructor(protected http: HttpClient) {}

  create(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    return this.http.post<IPaymentRequisition>(this.resourceUrl, paymentRequisition, { observe: 'response' });
  }

  update(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    return this.http.put<IPaymentRequisition>(this.resourceUrl, paymentRequisition, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentRequisition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
