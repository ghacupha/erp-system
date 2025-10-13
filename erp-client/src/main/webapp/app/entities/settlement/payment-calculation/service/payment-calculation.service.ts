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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPaymentCalculation, getPaymentCalculationIdentifier } from '../payment-calculation.model';

export type EntityResponseType = HttpResponse<IPaymentCalculation>;
export type EntityArrayResponseType = HttpResponse<IPaymentCalculation[]>;

@Injectable({ providedIn: 'root' })
export class PaymentCalculationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-calculations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/payment-calculations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    return this.http.post<IPaymentCalculation>(this.resourceUrl, paymentCalculation, { observe: 'response' });
  }

  update(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    return this.http.put<IPaymentCalculation>(
      `${this.resourceUrl}/${getPaymentCalculationIdentifier(paymentCalculation) as number}`,
      paymentCalculation,
      { observe: 'response' }
    );
  }

  partialUpdate(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    return this.http.patch<IPaymentCalculation>(
      `${this.resourceUrl}/${getPaymentCalculationIdentifier(paymentCalculation) as number}`,
      paymentCalculation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentCalculation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCalculation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCalculation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPaymentCalculationToCollectionIfMissing(
    paymentCalculationCollection: IPaymentCalculation[],
    ...paymentCalculationsToCheck: (IPaymentCalculation | null | undefined)[]
  ): IPaymentCalculation[] {
    const paymentCalculations: IPaymentCalculation[] = paymentCalculationsToCheck.filter(isPresent);
    if (paymentCalculations.length > 0) {
      const paymentCalculationCollectionIdentifiers = paymentCalculationCollection.map(
        paymentCalculationItem => getPaymentCalculationIdentifier(paymentCalculationItem)!
      );
      const paymentCalculationsToAdd = paymentCalculations.filter(paymentCalculationItem => {
        const paymentCalculationIdentifier = getPaymentCalculationIdentifier(paymentCalculationItem);
        if (paymentCalculationIdentifier == null || paymentCalculationCollectionIdentifiers.includes(paymentCalculationIdentifier)) {
          return false;
        }
        paymentCalculationCollectionIdentifiers.push(paymentCalculationIdentifier);
        return true;
      });
      return [...paymentCalculationsToAdd, ...paymentCalculationCollection];
    }
    return paymentCalculationCollection;
  }
}
