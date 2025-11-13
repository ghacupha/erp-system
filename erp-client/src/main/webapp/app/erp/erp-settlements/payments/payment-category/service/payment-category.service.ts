///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { IPaymentCategory, getPaymentCategoryIdentifier } from '../payment-category.model';

export type EntityResponseType = HttpResponse<IPaymentCategory>;
export type EntityArrayResponseType = HttpResponse<IPaymentCategory[]>;

@Injectable({ providedIn: 'root' })
export class PaymentCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/payment-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/payment-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentCategory: IPaymentCategory): Observable<EntityResponseType> {
    return this.http.post<IPaymentCategory>(this.resourceUrl, paymentCategory, { observe: 'response' });
  }

  update(paymentCategory: IPaymentCategory): Observable<EntityResponseType> {
    return this.http.put<IPaymentCategory>(
      `${this.resourceUrl}/${getPaymentCategoryIdentifier(paymentCategory) as number}`,
      paymentCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(paymentCategory: IPaymentCategory): Observable<EntityResponseType> {
    return this.http.patch<IPaymentCategory>(
      `${this.resourceUrl}/${getPaymentCategoryIdentifier(paymentCategory) as number}`,
      paymentCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPaymentCategoryToCollectionIfMissing(
    paymentCategoryCollection: IPaymentCategory[],
    ...paymentCategoriesToCheck: (IPaymentCategory | null | undefined)[]
  ): IPaymentCategory[] {
    const paymentCategories: IPaymentCategory[] = paymentCategoriesToCheck.filter(isPresent);
    if (paymentCategories.length > 0) {
      const paymentCategoryCollectionIdentifiers = paymentCategoryCollection.map(
        paymentCategoryItem => getPaymentCategoryIdentifier(paymentCategoryItem)!
      );
      const paymentCategoriesToAdd = paymentCategories.filter(paymentCategoryItem => {
        const paymentCategoryIdentifier = getPaymentCategoryIdentifier(paymentCategoryItem);
        if (paymentCategoryIdentifier == null || paymentCategoryCollectionIdentifiers.includes(paymentCategoryIdentifier)) {
          return false;
        }
        paymentCategoryCollectionIdentifiers.push(paymentCategoryIdentifier);
        return true;
      });
      return [...paymentCategoriesToAdd, ...paymentCategoryCollection];
    }
    return paymentCategoryCollection;
  }
}
