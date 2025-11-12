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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPaymentLabel, getPaymentLabelIdentifier } from '../payment-label.model';

export type EntityResponseType = HttpResponse<IPaymentLabel>;
export type EntityArrayResponseType = HttpResponse<IPaymentLabel[]>;

@Injectable({ providedIn: 'root' })
export class PaymentLabelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-labels');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/payment-labels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentLabel: IPaymentLabel): Observable<EntityResponseType> {
    return this.http.post<IPaymentLabel>(this.resourceUrl, paymentLabel, { observe: 'response' });
  }

  update(paymentLabel: IPaymentLabel): Observable<EntityResponseType> {
    return this.http.put<IPaymentLabel>(`${this.resourceUrl}/${getPaymentLabelIdentifier(paymentLabel) as number}`, paymentLabel, {
      observe: 'response',
    });
  }

  partialUpdate(paymentLabel: IPaymentLabel): Observable<EntityResponseType> {
    return this.http.patch<IPaymentLabel>(`${this.resourceUrl}/${getPaymentLabelIdentifier(paymentLabel) as number}`, paymentLabel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentLabel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentLabel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentLabel[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPaymentLabelToCollectionIfMissing(
    paymentLabelCollection: IPaymentLabel[],
    ...paymentLabelsToCheck: (IPaymentLabel | null | undefined)[]
  ): IPaymentLabel[] {
    const paymentLabels: IPaymentLabel[] = paymentLabelsToCheck.filter(isPresent);
    if (paymentLabels.length > 0) {
      const paymentLabelCollectionIdentifiers = paymentLabelCollection.map(
        paymentLabelItem => getPaymentLabelIdentifier(paymentLabelItem)!
      );
      const paymentLabelsToAdd = paymentLabels.filter(paymentLabelItem => {
        const paymentLabelIdentifier = getPaymentLabelIdentifier(paymentLabelItem);
        if (paymentLabelIdentifier == null || paymentLabelCollectionIdentifiers.includes(paymentLabelIdentifier)) {
          return false;
        }
        paymentLabelCollectionIdentifiers.push(paymentLabelIdentifier);
        return true;
      });
      return [...paymentLabelsToAdd, ...paymentLabelCollection];
    }
    return paymentLabelCollection;
  }
}
